# 贡献指南 | [Contribution Guide](./CONTRIBUTING.md)

## 配置你的 `IDEA`

1. 设置强制换行位置为 `140` 字符
    * `编辑器` -> `代码样式` -> `常规`
2. 设置制表符大小为 `4` 个空格，缩进为 `4` 个空格，连续缩进为 `4` 个空格
    * `编辑器` -> `代码样式` -> `Java` -> `制表符和缩进`
3. 设置将 `import` 与 `*` 搭配使用的类计数为 `9999`，设置将静态 `import` 与 `*` 搭配使用的类计数为 `9999`
    * `编辑器` -> `代码样式` -> `Java` -> `Import`

## 各类注解的使用

1. `org.jetbrains.annotations.NotNull` 表示该参数不能为 `null`，你应该修复所有未使用该注解所产生的 `IDEA` 提醒，包括段落前的 `@` 符号提醒和参数
    标黄的提醒
2. `org.jetbrains.annotations.Nullable` 表示该参数可以为 `null`，你应该修复所有未使用该注解所产生的 `IDEA` 提醒，包括段落前的 `@` 符号提醒和参数
    标黄的提醒

## 提交规范

1. 提交时，请使用 `git commit -m "commit message"` 的格式
2. 如果该 `commit` 是对于 `issue` 的解决或修复，请在 `commit message` 中填写 `#issue number`
3. 我们不限制在 `commit message` 中使用的语言，但请至少包含一个英文的简单描述，并将其放置在其它语言的描述之前
4. 如果你在 `dev.dubhe.anvilcraft.api` 软件包中添加了新的类和公开方法，请为他们添加完善的 `Javadoc` ，如果你修改了此软件包内的任何公开或非公开的
    方法，请保证它们的二进制兼容性

## Pull requests

1. `Pull requests` 标题应至少包含一个英文的简单描述，并将其放置在其它语言标题之前
2. 你应在 `Pull requests` 的描述中详细解释你所做的更改
3. 你应在 `Pull requests` 的描述中使用 `fixed` 或 `resolved` 链接对应的 `issue`
4. 以下是一个简单的 `Pull requests` 示例
    * > ### Fix drop of mob amber blocks 修复生物琥珀块挖掘掉落物问题
      > - 修复了 #1533 的问题。
      > - 现在挖掘含生物琥珀块时，会正确地保存其包含生物的数据。
      > - 修复了部分数据与语言文件的错误。
      > - fixed #1533
