package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.innerblock;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


public class SimpleBlockStateManager extends InnerBlockStateManager {

    @Override
    protected boolean isDefinitionEvent(final AstVisitEvent event) {
    		//TODO SimpelBlock��ϋɓI�Ɏ��ʂ�����@���K�v?
        AstToken parentToken = event.getParentToken();
        return  event.getToken().isBlock() && !parentToken.isBlockDefinition()
                && !parentToken.isSynchronized() && !parentToken.isInstantiation()
                && !parentToken.isEnumConstant() && !parentToken.isStaticInitializerDefinition()
                && !parentToken.isInstanceInitializerDefinition();
    }

}
