package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager.StateChangeEvent.StateChangeEventType;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * �N���X��`���Ƃ��̌�̃N���X�u���b�N�ɑ΂���r�W�^�[�̏�Ԃ��Ǘ����C��ԑJ�ڃC�x���g�𔭍s����N���X�D
 * 
 * @author kou-tngt
 *
 */
public class ClassDefinitionStateManager extends DeclaredBlockStateManager {

    /**
     * �N���X��`���Ƃ��̌�̃N���X�u���b�N�Ɋւ����ԑJ�ڃC�x���g�^�C�v��\��Enum
     * @author kou-tngt
     *
     */
    public static enum CLASS_STATE_CHANGE implements StateChangeEventType {
        ENTER_CLASS_DEF, EXIT_CLASS_DEF, ENTER_CLASS_BLOCK, EXIT_CLASS_BLOCK;
    }

    /**
     * �N���X�u���b�N�ɓ��������ɔ��s�����ԕω��C�x���g�^�C�v��Ԃ��D
     * @return �N���X�u���b�N�ɓ��������ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    @Override
    protected StateChangeEventType getBlockEnterEventType() {
        return CLASS_STATE_CHANGE.ENTER_CLASS_BLOCK;
    }

    /**
     * �N���X�u���b�N����o�����ɔ��s�����ԕω��C�x���g�^�C�v��Ԃ��D
     * @return �N���X�u���b�N����o�����ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    @Override
    protected StateChangeEventType getBlockExitEventType() {
        return CLASS_STATE_CHANGE.EXIT_CLASS_BLOCK;
    }

    /**
     * �N���X��`���ɓ��������ɔ��s�����ԕω��C�x���g�^�C�v��Ԃ��D
     * @return �N���X��`���ɓ��������ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    @Override
    protected StateChangeEventType getDefinitionEnterEventType() {
        return CLASS_STATE_CHANGE.ENTER_CLASS_DEF;
    }

    /**
     * �N���X��`������o�����ɔ��s�����ԕω��C�x���g�^�C�v��Ԃ��D
     * @return �N���X��`������o�����ɔ��s�����ԕω��C�x���g�̃C�x���g�^�C�v
     */
    @Override
    protected StateChangeEventType getDefinitionExitEventType() {
        return CLASS_STATE_CHANGE.EXIT_CLASS_DEF;
    }

    /**
     * �����̃C�x���g���N���X��`����\�����ǂ�����Ԃ��D
     * token.isClassDefinition()���\�b�h��p���Ĕ��肷��D
     * 
     * @param event�@�N���X��`����\�����ǂ����𒲂ׂ���AST�C�x���g
     * @return �N���X��`����\���g�[�N���ł����true
     */
    @Override
    protected boolean isDefinitionEvent(final AstVisitEvent event) {
        return event.getToken().isClassDefinition() || event.getToken().isEnumDefinition();
    }

    /**
     * �����̃g�[�N�����N���X�u���b�N��\�����ǂ�����Ԃ��D
     * token.isClassblock()���\�b�h��p���Ĕ��肷��D
     * 
     * @param token�@�N���X�u���b�N��\�����ǂ����𒲂ׂ���AST�g�[�N��
     * @return �N���X�u���b�N��\���g�[�N���ł����true
     */
    @Override
    protected boolean isBlockToken(final AstToken token) {
        return token.isClassBlock();
    }
}
