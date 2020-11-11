**Split SDK (Rider plugin)**

<!--[Download from JetBrains plugins repository.](https://plugins.jetbrains.com/plugin/15261-readable-bitwise-operations)-->

  Adds a .csproj quick fix that replace the SDK line in the file header with two import statements.

Turns this:
```xml
<Project Sdk="Microsoft.NET.Sdk">

</Project>
```
into this
```xml
<Project>
    <Import Project="Sdk.props" Sdk="Microsoft.NET.Sdk" />
    <Import Project="Sdk.targets" Sdk="Microsoft.NET.Sdk" />
</Project>
```
This is useful if you want to add code after the `Sdk.targets` line.