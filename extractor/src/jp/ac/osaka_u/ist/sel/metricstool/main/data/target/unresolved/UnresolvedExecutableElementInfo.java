package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExecutableElementInfo;


/**
 * ���s�\�ȒP�ʂ�\���v�f��\���C���^�[�t�F�[�X
 * 
 * @author higo
 *
 * @param <T> ���O���������ꂽ�^��\���^�p�����[�^
 */
public interface UnresolvedExecutableElementInfo<T extends ExecutableElementInfo> extends
        Resolvable<T>, PositionSetting, UnresolvedHavingOuterUnit {

}
