package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * �Ώۃ��\�b�h�̏���ۗL����N���X�D �ȉ��̏������D
 * <ul>
 * <li>���\�b�h��</li>
 * <li>�C���q</li>
 * <li>�Ԃ�l�̌^</li>
 * <li>�����̃��X�g</li>
 * <li>�s��</li>
 * <li>�R���g���[���O���t�i���΂炭�͖������j</li>
 * <li>���[�J���ϐ�</li>
 * <li>�������Ă���N���X</li>
 * <li>�Ăяo���Ă��郁�\�b�h</li>
 * <li>�Ăяo����Ă��郁�\�b�h</li>
 * <li>�I�[�o�[���C�h���Ă��郁�\�b�h</li>
 * <li>�I�[�o�[���C�h����Ă��郁�\�b�h</li>
 * <li>�Q�Ƃ��Ă���t�B�[���h</li>
 * <li>������Ă���t�B�[���h</li>
 * </ul>
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public final class TargetMethodInfo extends MethodInfo implements StaticOrInstance {

    /**
     * ���\�b�h�I�u�W�F�N�g������������D
     * 
     * @param modifiers �C���q
     * @param name ���\�b�h��
     * @param instance �C���X�^���X�����o�[���ǂ���
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetMethodInfo(final Set<ModifierInfo> modifiers, final String name,
            final boolean instance, final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {

        super(modifiers, name, instance, fromLine, fromColumn, toLine, toColumn);
    }
}
