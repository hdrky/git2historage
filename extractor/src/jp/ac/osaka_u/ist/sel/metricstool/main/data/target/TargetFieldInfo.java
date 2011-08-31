package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �ΏۃN���X�ɒ�`����Ă���t�B�[���h�̏��������N���X�D
 * 
 * @author higo
 */
@SuppressWarnings("serial")
public final class TargetFieldInfo extends FieldInfo {

    /**
     * �t�B�[���h���I�u�W�F�N�g��������
     * 
     * @param modifiers �C���q�� Set
     * @param name ���O
     * @param definitionClass ���̃t�B�[���h���`���Ă���N���X
     * @param instance �C���X�^���X�����o�[���ǂ���
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetFieldInfo(final Set<ModifierInfo> modifiers, final String name,
            final TargetClassInfo definitionClass, final boolean instance, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, name, definitionClass, instance, fromLine, fromColumn, toLine, toColumn);

        this.initializer = null;
    }

    /**
     * �t�B�[���h�̏����������Z�b�g����
     * 
     * @param initializer �t�B�[���h�̏�������
     */
    public final void setInitializer(final ExpressionInfo initializer) {
        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == initializer) {
            throw new IllegalArgumentException();
        }

        this.initializer = initializer;
    }

    /**
     * �t�B�[���h�̏���������Ԃ�
     * 
     * @return�@�t�B�[���h�̏�������
     */
    public final ExpressionInfo getInitializer() {
        return this.initializer;
    }

    private ExpressionInfo initializer;

}
