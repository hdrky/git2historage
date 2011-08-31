package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;

public class ConstructorStateManager extends CallableUnitStateManager {

    /**
     * �����̃C�x���g���R���X�g���N�^��`����\�����ǂ�����Ԃ��D
     * token.isConstructorDefinition()���\�b�h��p���Ĕ��肷��D
     * 
     * @param event�@�R���X�g���N�^��`����\�����ǂ����𒲂ׂ���AST�C�x���g
     * @return �R���X�g���N�^��`����\���g�[�N���ł����true
     */
    @Override
    protected boolean isDefinitionEvent(final AstVisitEvent event) {
        return event.getToken().isConstructorDefinition();
    }

}
