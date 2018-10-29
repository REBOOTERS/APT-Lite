# APT-Lite
Apt 


- cmd line
```
./gradlew :example:complileDebugJavaWithJavac
```
 or 
 
 ```
 ./gradlew :example:compileReleaseJavaWithJavac
```

使用上述命令，可以查看 apt-library 编译期的日志，即 BindViewProcessor 类中 process 方法执行时打印的日志
（使用之前需要 gradle clean）