package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * �r�W�^�[�̃u���b�N�ւ̏o������Ǘ�����X�e�[�g�}�l�[�W���D
 * 
 * �r�W�^�[���u���b�N��\��AST�g�[�N���̒��ɏo���肷��ۂɁC���BLOCK_STATE_CHANGE�̏�ԕω��C�x���g��ʒm����D
 * 
 * @author kou-tngt
 *
 */
public class BlockStateManager extends EnterExitStateManager {

    /**
     * �u���b�N�֏o���肷��ۂɔ��s������ԕω��C�x���g�̃C�x���g�^�C�v
     * @author kou-tngt
     *
     */
    public static enum BLOCK_STATE_CHANGE implements StateChangeEventType {
        ENTER, EXIT
    };

    /**
     * �u���b�N�̒��ɓ��������ɒʒm������ԕω��C�x���g�̎�ނ�Ԃ��D
     * @return�@�u���b�N�̒��ɓ��������ɒʒm������ԕω��C�x���g�̎��
     */
    @Override
    public StateChangeEventType getEnterEventType() {
        return BLOCK_STATE_CHANGE.ENTER;
    }

    /**
     * �u���b�N����o�����ɒʒm������ԕω��C�x���g�̎�ނ�Ԃ��D
     * @return�@�u���b�N����o�����ɒʒm������ԕω��C�x���g�̎��
     */
    @Override
    public StateChangeEventType getExitEventType() {
        return BLOCK_STATE_CHANGE.EXIT;
    }

    /**
     * �����ŗ^����ꂽ�C�x���g���u���b�N��\�����ǂ�����Ԃ�.
     * token.isBlock()��true�̏ꍇ��true,false�̏ꍇ��false��Ԃ��D
     * 
     * @param event �u���b�N��\�����ǂ����𒲂ׂ�C�x���g
     * @return token.isBlock()��true�̏ꍇ��true,false�̏ꍇ��false
     */
    @Override
    protected boolean isStateChangeTriggerEvent(final AstVisitEvent event) {
        return event.getToken().isBlock();
    }

}
