# APT-Lite


### how to debug this

- first  

[创建 debug ](https://uploader.shimo.im/f/QGbFORbArucgvQkS.png)

- gradle.properties 中添加
```groovy
org.gradle.jvmargs=-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005
-Dorg.gradle.debug=true
```

- 以 debug 模式运行上述进程，然后 run 或者是执行以下命令（节省时间）
- execute below 「content」 at cmd line
```
./gradlew :example:compileDebugJavaWithJavac
```
 or 
 
 ```
 ./gradlew :example:compileReleaseJavaWithJavac
```

使用上述命令，可以查看 apt-library 编译期的日志，即 BindViewProcessor 类中 process 方法执行时打印的日志
（使用之前需要 gradle clean）

> apt 生成的文件一般在 module/build/generated/source/apt/debug/package 目录下