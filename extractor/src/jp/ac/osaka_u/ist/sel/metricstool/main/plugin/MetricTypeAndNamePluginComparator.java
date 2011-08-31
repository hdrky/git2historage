package jp.ac.osaka_u.ist.sel.metricstool.main.plugin;


import java.util.Comparator;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;


/**
 * �v���O�C�����r����R���p���[�^.
 * ���g���N�X�̃^�C�v->���g���N�X��->�N���X���̏��Ŕ�r����.
 * �N���X���܂ň�v����v���O�C���͓���Ɣ��肷��.
 * @author kou-tngt
 *
 */
public class MetricTypeAndNamePluginComparator implements Comparator<AbstractPlugin> {

    /**
     * ���g���N�X�̃^�C�v->���g���N�X��->�N���X���̏��Ŕ�r����.
     * �N���X���܂ň�v����v���O�C���͓���Ɣ��肷��.
     * @param o1 ��r����v���O�C��
     * @param o2 ��r����v���O�C��
     * @return o1��o2���������I�ɏ�������Ε��̐��C�����ł����0�C�傫����ΐ��̐���Ԃ�.
     */
    public int compare(final AbstractPlugin o1, final AbstractPlugin o2) {
        final PluginInfo info1 = o1.getPluginInfo();
        final PluginInfo info2 = o2.getPluginInfo();

        int result = info1.getMetricType().compareTo(info2.getMetricType());
        if (0 != result) {
            return result;
        }

        result = info1.getMetricName().compareTo(info2.getMetricName());
        if (0 != result) {
            return result;
        }

        return o1.getClass().getCanonicalName().compareTo(o2.getClass().getCanonicalName());
    }
}
