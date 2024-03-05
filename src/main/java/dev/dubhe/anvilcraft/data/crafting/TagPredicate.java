package dev.dubhe.anvilcraft.data.crafting;

import com.google.gson.*;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.network.FriendlyByteBuf;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TagPredicate implements Predicate<CompoundTag> {
    public static final TagPredicate EMPTY = new TagPredicate(new ArrayList<>());
    public final List<CompoundTag> tags;
    private String[] stringTags = null;

    private TagPredicate(List<CompoundTag> tags) {
        this.tags = tags;
    }

    public static TagPredicate of(CompoundTag... tags) {
        return new TagPredicate(Arrays.asList(tags));
    }

    @Override
    public boolean test(CompoundTag tag) {
        if (tags.isEmpty()) return true;
        for (CompoundTag tag1 : tags) {
            if (matchesContainTag(tag1, tag)) return true;
        }
        return false;
    }

    static boolean matchesContainTag(@NotNull CompoundTag tag, @NotNull CompoundTag tag1) {
        boolean bl = true;
        for (String key : tag.getAllKeys()) {
            if (!tag1.contains(key)) {
                bl = false;
                break;
            }
            if (!Objects.equals(tag.get(key), tag1.get(key))) {
                bl = false;
                break;
            }
        }
        return bl;
    }

    static TagPredicate fromJson(JsonElement json) {
        if (null == json || json.isJsonNull()) {
            throw new JsonSyntaxException("Item cannot be null");
        }
        if (json.isJsonObject()) {
            JsonObject object = json.getAsJsonObject();
            if (!object.has("data")) return TagPredicate.EMPTY;
            JsonElement data = object.get("data");
            if (!data.isJsonPrimitive()) throw new JsonSyntaxException("Expected item to be string");
            return new TagPredicate(List.of(TagPredicate.parseTag(data.getAsString())));
        }
        if (json.isJsonArray()) {
            JsonArray array = json.getAsJsonArray();
            List<CompoundTag> list = new ArrayList<>();
            for (JsonElement element : array) {
                if (json.isJsonObject()) {
                    JsonObject object = element.getAsJsonObject();
                    if (!object.has("data")) continue;
                    JsonElement data = object.get("data");
                    if (!data.isJsonPrimitive() && !data.isJsonArray())
                        throw new JsonSyntaxException("Expected item to be string");
                    if (data.isJsonPrimitive()) list.add(TagPredicate.parseTag(data.getAsString()));
                    else for (JsonElement elem : data.getAsJsonArray()) {
                        if (elem.isJsonPrimitive()) list.add(TagPredicate.parseTag(elem.getAsString()));
                    }
                } else throw new JsonSyntaxException("Expected item to be object or array of objects");
            }
            return new TagPredicate(list);
        }
        throw new JsonSyntaxException("Expected item to be object or array of objects");
    }

    public JsonElement toJson() {
        if (this.tags.size() == 1) {
            return new JsonPrimitive(this.tags.get(0).toString());
        }
        JsonArray jsonArray = new JsonArray();
        for (CompoundTag value : this.tags) {
            jsonArray.add(value.toString());
        }
        return jsonArray;
    }

    public static @NotNull TagPredicate fromNetwork(@NotNull FriendlyByteBuf buffer) {
        return TagPredicate.fromValues(buffer.readList(FriendlyByteBuf::readUtf).stream().map(TagPredicate::parseTag));
    }

    static @NotNull CompoundTag parseTag(String string) {
        try {
            return TagParser.parseTag(string);
        } catch (CommandSyntaxException ignored) {
        }
        throw new JsonSyntaxException("Invalid NBT string");
    }

    private static @NotNull TagPredicate fromValues(@NotNull Stream<CompoundTag> stream) {
        return new TagPredicate(stream.toList());
    }

    public void toNetwork(@NotNull FriendlyByteBuf buffer) {
        buffer.writeCollection(Arrays.asList(this.getStringTags()), FriendlyByteBuf::writeUtf);
    }

    public String[] getStringTags() {
        if (null == this.stringTags) {
            this.stringTags = this.tags.stream().map(CompoundTag::toString).distinct().toArray(String[]::new);
        }
        return this.stringTags;
    }
}
