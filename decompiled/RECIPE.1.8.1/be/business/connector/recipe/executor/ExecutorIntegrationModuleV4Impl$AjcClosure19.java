package be.business.connector.recipe.executor;

import org.aspectj.lang.JoinPoint;
import org.aspectj.runtime.internal.AroundClosure;

public class ExecutorIntegrationModuleV4Impl$AjcClosure19 extends AroundClosure {
   public ExecutorIntegrationModuleV4Impl$AjcClosure19(Object[] var1) {
      super(var1);
   }

   public Object run(Object[] var1) {
      Object[] var2 = super.state;
      ExecutorIntegrationModuleV4Impl.createFeedback_aroundBody18((ExecutorIntegrationModuleV4Impl)var2[0], (String)var2[1], (String)var2[2], (byte[])var2[3], (JoinPoint)var2[4]);
      return null;
   }
}
