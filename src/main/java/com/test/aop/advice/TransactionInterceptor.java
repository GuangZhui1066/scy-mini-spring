package com.test.aop.advice;

import com.minis.aop.MethodInterceptor;
import com.minis.aop.MethodInvocation;

/**
 * 用 AOP 实现事务处理
 */
public class TransactionInterceptor implements MethodInterceptor {

    TransactionManager transactionManager;

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    // todo：conn在外层，事务并没有生效
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("TransactionInterceptor begin transaction ...");
        transactionManager.doBegin();
        Object result = invocation.proceed();
        transactionManager.doCommit();
        //transactionManager.doRollBack();
        System.out.println("TransactionInterceptor transaction committed ...");
        return result;
    }

}
