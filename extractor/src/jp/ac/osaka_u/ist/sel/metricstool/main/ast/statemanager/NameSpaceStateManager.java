package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * ���O��Ԓ�`���Ƃ��̌�̃u���b�N�ɑ΂���r�W�^�[�̏�Ԃ��Ǘ����C��ԑJ�ڃC�x���g�𔭍s����N���X�D
 * 
 * @author kou-tngt
 *
 */
public class NameSpaceStateManager extends DeclaredBlockStateManager {

    /**
     * ���s�����ԑJ�ڃC�x���g�̎�ނ�\��Enum
     * 
     * @author kou-tngt
     *
     */
    public static enum NAMESPACE_STATE_CHANGE implements StateChangeEventType {
        ENTER_NAMESPACE_DEF, EXIT_NAMESPACE_DEF,

        ENTER_NAMESPACE_BLOCK, EXIT_NAMESPACE_BLOCK, ;
    }

    /**
     * ���O��Ԓ�`���ɑ����u���b�N�ɓ��������ɔ��s�����ԕω��C�x���g�^�C�v��Ԃ��D
     * @return ���O��Ԓ�`���ɑ����u���b�N�ɓ��������ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    @Override
    protected StateChangeEventType getBlockEnterEventType() {
        return NAMESPACE_STATE_CHANGE.ENTER_NAMESPACE_BLOCK;
    }

    /**
     * ���O��Ԓ�`���ɑ����u���b�N����o�����ɔ��s�����ԕω��C�x���g�^�C�v��Ԃ��D
     * @return ���O��Ԓ�`���ɑ����u���b�N����o�����ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    @Override
    protected StateChangeEventType getBlockExitEventType() {
        return NAMESPACE_STATE_CHANGE.EXIT_NAMESPACE_BLOCK;
    }

    /**
     * ���O��Ԓ�`���ɓ��������ɔ��s�����ԕω��C�x���g�^�C�v��Ԃ��D
     * @return ���O��Ԓ�`���ɂɓ��������ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    @Override
    protected StateChangeEventType getDefinitionEnterEventType() {
        return NAMESPACE_STATE_CHANGE.ENTER_NAMESPACE_DEF;
    }

    /**
     * ���O��Ԓ�`������o�����ɔ��s�����ԕω��C�x���g�^�C�v��Ԃ��D
     * @return ���O��Ԓ�`������o�����ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    @Override
    protected StateChangeEventType getDefinitionExitEventType() {
        return NAMESPACE_STATE_CHANGE.EXIT_NAMESPACE_DEF;
    }

    /**
     * �����̃C�x���g�����O��Ԓ�`����\�����ǂ�����Ԃ��D
     * token.isNameSpaceDefinition()���\�b�h��p���Ĕ��肷��D
     * 
     * @param token�@���O��Ԓ�`����\�����ǂ����𒲂ׂ���AST�C�x���g
     * @return ���O��Ԓ�`����\���g�[�N���ł����true
     */
    @Override
    protected boolean isDefinitionEvent(final AstVisitEvent event) {
        return event.getToken().isNameSpaceDefinition();
    }
}
