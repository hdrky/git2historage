package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.Set;


/**
 * ���͑ΏۃR���X�g���N�^��\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class TargetConstructorInfo extends ConstructorInfo {

    /**
     * �K�v�ȏ���^���ăI�u�W�F�N�g��������
     * 
     * @param modifiers �C���q�̃Z�b�g
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public TargetConstructorInfo(final Set<ModifierInfo> modifiers, final int fromLine,
            final int fromColumn, final int toLine, final int toColumn) {

        super(modifiers, fromLine, fromColumn, toLine, toColumn);
    }
}
