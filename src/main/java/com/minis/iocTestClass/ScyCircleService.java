package com.minis.iocTestClass;

import com.minis.beans.factory.annotation.Autowired;

/**
 * 测试循环依赖
 *   ScyTestService 依赖 ScyBaseService，ScyBaseService 又依赖 ScyCircleService，ScyCircleService 又依赖 ScyTestService，
 *
 * 问题：
 *   在实例化 ScyTestService 时，其依赖 ScyBaseService，因为bean仓库中没有 ScyBaseService 的实例，所以 mini-Spring 会先去实例化 ScyBaseService；
 *   在实例化 ScyBaseService 时，其依赖 ScyCircleService，因为bean仓库中没有 ScyCircleService 的实例，所以 mini-Spring 会先去实例化 ScyCircleService；
 *   在实例化 ScyCircleService 时，其依赖 ScyTestService。但此时因为 ScyTestService 还没有实例化完成，所以并没有被注册到 bean 仓库中，所以 mini-Spring 又会去实例化 ScyTestService；
 *   这样就造成了死循环。
 *
 * 解决办法：
 *   在实例化上面的几个 bean 时，都是已经用构造器创建出了实例化对象，但是只为其注入了部分属性，没有完全实例化完成，所以它们没有被注册到 bean 的仓库中，
 *   导致在实例化依赖它的 bean 时，无法从 bean 仓库获取到已经创建好的对象，又去重新创建新的对象。
 *   所以我们在bean仓库中引入 earlySingletonObjects，存放那些已经创建出、但是没有完全注入所有属性的毛坯实例。
 *   这样，三个循环依赖的 bean 的创建过程如下：
 *     1. 先实例化 ScyTestService (只对它注入了部分属性，还没有注入 baseService 属性)，然后把它放入 earlySingletonObjects
 *     2. 再实例化 ScyBaseService (还没有注入 circleService 属性)，然后把它放入 earlySingletonObjects
 *     3. 再实例化 ScyCircleService，其依赖 testService，我们从 earlySingletonObjects 中取出之前创建出的毛坯 scyTestServcie 实例为 ScyCircleService 的 testService 属性赋值。这样就创建好了 scyCircleService.
 *     4. 为实例化出的 ScyBaseService 对象注入其 circleService 属性，完成 ScyBaseService 的实例化
 *     5. 为实例化出的 ScyTestService 对象注入其 baseService 属性，完成 ScyTestService 的实例化
 *     6. 至此，完成了三个bean的实例化～～
 *
 * 思考：
 *   循环依赖只能通过属性注入的方式去解，用构造器注入的方式无法解决循环依赖的问题
 *   所以 Spring 不支持构造器注入的循环依赖
 */
public class ScyCircleService {

    public ScyCircleService() {
        System.out.println("ScyCircleService constructor. no arguments.");
    }

    @Autowired
    private ScyTestServiceImpl scyTestService;

    public ScyTestService getScyTestService() {
        return scyTestService;
    }

    public void setScyTestService(ScyTestServiceImpl scyTestService) {
        this.scyTestService = scyTestService;
        System.out.println("ScyCircleService setTestService done.");
    }

}
