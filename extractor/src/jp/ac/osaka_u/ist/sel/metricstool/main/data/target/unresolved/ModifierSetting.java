package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.Modifier;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ModifierInfo;


/**
 * �C���q���Z�b�g���邽�߂̃C���^�[�t�F�[�X
 * 
 * @author higo
 *
 */
public interface ModifierSetting extends Modifier {

    /**
     * �����ŗ^����ꂽ�C���q��ǉ�����
     * 
     * @param modifier �ǉ�����C���q
     */
    void addModifier(final ModifierInfo modifier);
}
