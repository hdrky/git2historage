package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * AST�r�W�^�[���C���q�L�q���ɓ��B�������ɏ�ԑJ�ڂ��C��ԕω��C�x���g�𔭍s����X�e�[�g�}�l�[�W���D
 * 
 * @author kou-tngt
 *
 */
public class ModifiersDefinitionStateManager extends EnterExitStateManager {

    /**
     * �ʒm�����ԕω��C�x���g�̎�ނ�\��enum
     * 
     * @author kou-tngt
     *
     */
    public static enum MODIFIERS_STATE implements StateChangeEventType {
        ENTER_MODIFIERS_DEF, EXIT_MODIFIERS_DEF
    }

    /**
     * �C���q�L�q���̒��ɓ��������̏�ԕω��C�x���g�̎�ނ�Ԃ��D
     * @return�@�C���q�L�q���̒��ɓ��������̏�ԕω��C�x���g�̎��
     */
    @Override
    public StateChangeEventType getEnterEventType() {
        return MODIFIERS_STATE.ENTER_MODIFIERS_DEF;
    }

    /**
     * �C���q�L�q������o�����̏�ԕω��C�x���g�̎�ނ�Ԃ��D
     * @return�@�C���q�L�q������o�����̏�ԕω��C�x���g�̎��
     */
    @Override
    public StateChangeEventType getExitEventType() {
        return MODIFIERS_STATE.EXIT_MODIFIERS_DEF;
    }

    /**
     * �����ŗ^����ꂽ�C�x���g���C���q�L�q����\�����ǂ�����Ԃ�.
     * ����ɂ�token.isModifiersDefinition()���\�b�h��p����D
     * 
     * @param event �C���q�L�q����\�����ǂ����𒲂ׂ�C�x���g
     * @return �C���q�L�q����\���ꍇ��true
     */
    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        return event.getToken().isModifiersDefinition();
    }
}
