# Contribution Guide | [贡献指南](./CONTRIBUTING.cn.md)

## Configure your `IDEA`

1. Set the hard wrap at position to `140` columns.
    * `Editor` -> `Code Style` -> `General`
2. Set tab size to `4` spaces, indent to `4` spaces, and continuation indent to `4` spaces.
    * `Editor` -> `Code Style` -> `Java` -> `Tabs and Indents`
3. Set the class count to use `import` with `*` to `9999`, and set the names count to use static `import` with `*` to `9999`.
    * `Editor` -> `Code Style` -> `Java` -> `Imports`

## The use of various annotations

1. `org.jetbrains.annotations.NotNull` indicates that the parameter cannot be `null`. You should address all `IDEA` warnings caused by not
    using this annotation, including the `@` symbol warnings before a paragraph and yellow-highlighted parameter warnings.
2. `org.jetbrains.annotations.Nullable` indicates that the parameter can be `null`. You should address all `IDEA` warnings caused by not
    using this annotation, including the `@` symbol warnings before a paragraph and yellow-highlighted parameter warnings.

## Submit specifications

1. When committing, please use the format `git commit -m "commit message"`.
2. If the `commit` is for resolving or fixing an `issue`, please include `#issue number` in the `commit message`.
3. We do not restrict the language used in the `commit message`, but please include at least a simple English description and place it
    before descriptions in other languages.
4. If you add new classes or public methods in the `dev.dubhe.anvilcraft.api` package, please provide complete `Javadoc` documentation for
    them. If you modify any public or non-public methods in this package, ensure their binary compatibility.

## Pull requests

1. The title of a `Pull Request` should include at least a simple English description and place it before titles in other languages.
2. You should provide a detailed explanation of your changes in the `Pull Request` description.
3. Use `fixed` or `resolved` in the `Pull Request` description to link the corresponding `issue`.
4. Below is a simple `Pull Request` example:
    * > ### Fix drop of mob amber blocks
      > - fixed #1533
      > - When mining mob amber block now, the data containing mob will be correctly saved.
      > - Fixed some errors in data and language files.
