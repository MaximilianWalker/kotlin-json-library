# JSON Library

A Kotlin-based JSON library for serialization, deserialization, and validation of JSON data. This library provides a robust and extensible framework for working with JSON elements, including support for custom serialization options, data class inference, and validation.

## Features

- **Serialization**: Convert Kotlin objects to JSON strings.
- **Deserialization**: Parse JSON strings into Kotlin objects.
- **Validation**: Validate JSON arrays and other elements.
- **Data Class Inference**: Automatically map JSON objects to Kotlin data classes.
- **Customizable Serialization Options**: Pretty-printing, key sorting, and indentation.

## Installation

Add the following dependency to your `pom.xml` if you're using Maven:

```xml
<dependency>
    <groupId>pt.iscte.pa</groupId>
    <artifactId>JSON_Library</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

Ensure you have the Kotlin Maven plugin configured in your `pom.xml`:

```xml
<plugin>
    <groupId>org.jetbrains.kotlin</groupId>
    <artifactId>kotlin-maven-plugin</artifactId>
    <version>2.1.10</version>
    <executions>
        <execution>
            <id>compile</id>
            <phase>compile</phase>
            <goals>
                <goal>compile</goal>
            </goals>
        </execution>
        <execution>
            <id>test-compile</id>
            <phase>test-compile</phase>
            <goals>
                <goal>test-compile</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

## Usage

### Serialization

Convert Kotlin objects to JSON strings:

```kotlin
import core.*
import serializer.JSONSerializationOptions
import converter.JSONConverter

fun main() {
    val converter = JSONConverter()
    val jsonObject = JSONObject("name" to JSONString("John"), "age" to JSONNumber(30))
    val jsonString = converter.serializeObject(jsonObject, JSONSerializationOptions(prettyPrint = true))
    println(jsonString)
}
```

### Deserialization

Parse JSON strings into Kotlin objects:

```kotlin
import core.*
import converter.JSONConverter

fun main() {
    val converter = JSONConverter()
    val jsonString = """{"name":"John","age":30}"""
    val jsonObject = converter.deserializeObject(jsonString)
    println(jsonObject)
}
```

### Validation

Validate JSON arrays for consistent types:

```kotlin
import core.*
import validators.JSONArrayTypesValidator

fun main() {
    val jsonArray = JSONArray(JSONNumber(1), JSONNumber(2), JSONNumber(3))
    val validator = JSONArrayTypesValidator()
    println(validator.isValid(jsonArray)) // true
}
```

### Data Class Inference

Automatically map JSON objects to Kotlin data classes:

```kotlin
import core.*
import inference.JSONInference

data class Person(val name: String, val age: Int)

fun main() {
    val jsonObject = JSONObject("name" to JSONString("Alice"), "age" to JSONNumber(25))
    val person = JSONInference.convertTo<Person>(jsonObject)
    println(person)
}
```

### Custom Serialization Options

Customize serialization with options like pretty-printing and key sorting:

```kotlin
import core.*
import serializer.JSONSerializationOptions
import converter.JSONConverter

fun main() {
    val converter = JSONConverter()
    val jsonObject = JSONObject("bKey" to JSONNumber(2), "aKey" to JSONNumber(1))
    val options = JSONSerializationOptions(prettyPrint = true, sortKeys = true)
    val jsonString = converter.serializeObject(jsonObject, options)
    println(jsonString)
}
```

## Testing

The library includes comprehensive unit tests for all major features. To run the tests, use the following Maven command:

```bash
mvn test
```

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request with your changes.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.