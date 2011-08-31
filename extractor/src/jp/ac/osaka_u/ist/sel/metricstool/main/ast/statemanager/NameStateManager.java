package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * �r�W�^�[�̖��O�L�q���ւ̏o������Ǘ�����X�e�[�g�}�l�[�W���D
 * 
 * 
 * @author kou-tngt
 *
 */
public class NameStateManager extends EnterExitStateManager {
    public static enum NAME_STATE implements StateChangeEventType {
        ENTER_NAME, EXIT_NAME
    }

    /**
     * ���O�L�q���̒��ɓ��������ɒʒm����邽�߂̏�ԕω��C�x���g�̎�ނ�Ԃ��D
     * 
     * @return�@���O�L�q���̒��ɓ��������ɒʒm����邽�߂̏�ԕω��C�x���g�̎��
     */
    @Override
    public StateChangeEventType getEnterEventType() {
        return NAME_STATE.ENTER_NAME;
    }

    /**
     * ���O�L�q������o�����ɒʒm����邽�߂̏�ԕω��C�x���g�̎�ނ�Ԃ��D
     * 
     * @return�@���O�L�q������o�����ɒʒm����邽�߂̏�ԕω��C�x���g�̎��
     */
    @Override
    public StateChangeEventType getExitEventType() {
        return NAME_STATE.EXIT_NAME;
    }

    /**
     * �����ŗ^����ꂽ�C�x���g�����O�L�q����\�����ǂ�����Ԃ�.
     * token.isNameDescription()��true�ł����true��Ԃ��D
     * 
     * @param event ���O�L�q����\�����ǂ����𒲂ׂ�C�x���g
     * @return token.isNameDescription()��true�ł����true
     */
    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        return event.getToken().isNameDescription();
    }

}
