SPI 全称为 (Service Provider Interface) 

SPI 是 JDK 内置的一种服务提供发现机制。

SPI 是一种动态替换发现的机制，比如有个接口，想运行时动态的给它添加实现，你只需要添加一个实现。

我们经常遇到的就是 java.sql.Driver 接口，其他不同厂商可以针对同一接口做出不同的实现，`mysql` 和 `postgresql` 都有不同的实现提供给用户，而 Java 的 SPI 机制可以为某个接口寻找服务实现。

当服务的提供者提供了一种接口的实现之后，需要在 classpath 下的 `META-INF/services/` 目录里创建一个以服务接口命名的文件，这个文件里的内容就是这个接口的具体的实现类。当其他的程序需要这个服务的时候，就可以通过查找这个jar包（一般都是以 jar 包做依赖）的 `META-INF/services/` 中的配置文件，配置文件中有接口的具体实现类名，可以根据这个类名进行加载实例化，就可以使用该服务了。JDK中查找服务实现的工具类是：`java.util.ServiceLoader`。