package jp.ac.osaka_u.ist.sel.metricstool.main.util;


/**
 * ����̃t�B���^.
 * 
 * ���̃C���^�[�t�F�[�X�̃C���X�^���X��
 * {@link LanguageUtil#filterLanguages(LanguageFilter)} �ɓn�����Ƃ��ł���.
 * 
 * @author rniitani
 */
public interface LanguageFilter {
    /**
     * �w�肳�ꂽ���ꂪ���ꃊ�X�g�Ɋ܂܂��K�v�����邩�ǂ����𔻒肷��.
     * 
     * @param language �e�X�g�Ώۂ̌���
     * @return language ���܂܂��K�v������ꍇ true
     */
    boolean accept(LANGUAGE language);
}
