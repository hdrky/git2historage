package jp.ac.osaka_u.ist.sel.metricstool.main.util;


import java.util.Comparator;


/**
 * String�̔z����r���邽�߂̃N���X
 * 
 * @author higo
 */
public class StringArrayComparator implements Comparator<String[]> {

    /**
     * ���̃N���X�̒P��I�u�W�F�N�g�D
     * �����Ȃ��R���X�g���N�^�� private �Ő錾����Ă��邽�ߐV���ɃI�u�W�F�N�g���쐬���邱�Ƃ͂ł��Ȃ��D
     */
    public static final StringArrayComparator SINGLETON = new StringArrayComparator();

    /**
     * ���String[]���r����
     */
    public int compare(final String[] array1, final String[] array2) {

        if ((null == array1) || (null == array2)) {
            throw new IllegalArgumentException();
        }

        for (int index = 0;; index++) {

            if ((array1.length <= index) && (array2.length <= index)) {
                return 0;

            } else if ((array1.length <= index) && (index < array2.length)) {
                return -1;

            } else if ((index < array1.length) && (array2.length <= index)) {
                return 1;
            } else {
                final int order = array1[index].compareTo(array2[index]);
                if (0 != order) {
                    return order;
                }
            }
        }
    }

    /**
     * �V���O���g���p�^�[�����g���Ă��邽�߂� private �ɂ��Ă���
     */
    private StringArrayComparator() {
    }
}
