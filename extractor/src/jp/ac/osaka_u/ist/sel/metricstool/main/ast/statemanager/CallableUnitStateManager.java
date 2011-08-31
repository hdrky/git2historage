package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;


/**
 * ���\�b�h��`���Ƃ��̌�̃u���b�N�ɑ΂���r�W�^�[�̏�Ԃ��Ǘ����C��ԑJ�ڃC�x���g�𔭍s����N���X�D
 * 
 * @author kou-tngt
 *
 */
public abstract class CallableUnitStateManager extends DeclaredBlockStateManager {

    /**
     * ���s�����ԑJ�ڃC�x���g�̎�ނ�\��Enum
     * 
     * @author kou-tngt
     *
     */
    public static enum CALLABLE_UNIT_STATE_CHANGE implements StateChangeEventType {
        ENTER_DEF, EXIT_DEF,

        ENTER_BLOCK, EXIT_BLOCK, ;
    }

    /**
     * ���\�b�h��`���ɑ����u���b�N�ɓ��������ɔ��s�����ԕω��C�x���g�^�C�v��Ԃ��D
     * @return ���\�b�h��`���ɑ����u���b�N�ɓ��������ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    @Override
    protected StateChangeEventType getBlockEnterEventType() {
        return CALLABLE_UNIT_STATE_CHANGE.ENTER_BLOCK;
    }

    /**
     * ���\�b�h��`���ɑ����u���b�N����o�����ɔ��s�����ԕω��C�x���g�^�C�v��Ԃ��D
     * @return ���\�b�h��`���ɑ����u���b�N����o�����ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    @Override
    protected StateChangeEventType getBlockExitEventType() {
        return CALLABLE_UNIT_STATE_CHANGE.EXIT_BLOCK;
    }

    /**
     * ���\�b�h��`���ɓ��������ɔ��s�����ԕω��C�x���g�^�C�v��Ԃ��D
     * @return ���\�b�h��`���ɂɓ��������ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    @Override
    protected StateChangeEventType getDefinitionEnterEventType() {
        return CALLABLE_UNIT_STATE_CHANGE.ENTER_DEF;
    }

    /**
     * ���\�b�h��`������o�����ɔ��s�����ԕω��C�x���g�^�C�v��Ԃ��D
     * @return ���\�b�h��`������o�����ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    @Override
    protected StateChangeEventType getDefinitionExitEventType() {
        return CALLABLE_UNIT_STATE_CHANGE.EXIT_DEF;
    }



}
