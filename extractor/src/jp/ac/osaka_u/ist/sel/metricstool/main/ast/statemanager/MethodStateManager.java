package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class MethodStateManager extends CallableUnitStateManager {

    /**
     * �����̃C�x���g�����\�b�h��`����\�����ǂ�����Ԃ��D
     * token.isMethodDefinition()���\�b�h��p���Ĕ��肷��D
     * 
     * @param event ���\�b�h��`����\�����ǂ����𒲂ׂ���AST�C�x���g
     * @return ���\�b�h��`����\���g�[�N���ł����true
     */
    @Override
    protected boolean isDefinitionEvent(final AstVisitEvent event) {
        return event.getToken().isMethodDefinition();
    }

}
