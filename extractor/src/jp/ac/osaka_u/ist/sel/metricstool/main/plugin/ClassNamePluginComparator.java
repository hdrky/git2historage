package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.util.Comparator;


/**
 * �v���O�C���C���X�^���X�̃R���p���[�^.
 * �v���O�C���̃N���X���݂̂Ŕ�r����.
 * @author kou-tngt
 *
 */
public class ClassNamePluginComparator implements Comparator<AbstractPlugin> {

    /**
     * �v���O�C���C���X�^���X�̃N���X���݂̂Ŕ�r����.
     * @param o1 ��r����v���O�C��
     * @param o2 ��r����v���O�C��
     * @return o1��o2���������I�ɏ�������Ε��̐��C�����ł����0�C�傫����ΐ��̐���Ԃ�.
     */
    public int compare(final AbstractPlugin o1, final AbstractPlugin o2) {
        return o1.getClass().getCanonicalName().compareTo(o2.getClass().getCanonicalName());
    }

}
