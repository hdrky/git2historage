package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


/**
 * �X�^�e�B�b�N�C�j�V�����C�U��\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class StaticInitializerInfo extends InitializerInfo {

    /**
     * �K�v�ȏ���^���āC�I�u�W�F�N�g��������
     * 
     * @param fromLine �J�n�s
     * @param fromColumn �J�n��
     * @param toLine �I���s
     * @param toColumn �I����
     */
    public StaticInitializerInfo(final int fromLine, final int fromColumn, final int toLine,
            final int toColumn) {
        super(fromLine, fromColumn, toLine, toColumn);
    }
}
