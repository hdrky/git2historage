package jp.ac.osaka_u.ist.sel.metricstool.main.util;


import java.util.LinkedHashSet;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;


/**
 * {@link LANGUAGE} �̃��[�e�B���e�B�N���X.
 * 
 * @author rniitani
 */
public class LanguageUtil {

    /**
     * �S�Ă̌��ꂩ�� filter �ɂ���ă}�b�`��������݂̂�Ԃ�.
     * 
     * {@link AbstractPlugin#getMeasurableLanguages()} �ȂǂŎg�p����.
     * 
     * @param filter ����̃t�B���^
     * @return �t�B���^���ꂽ����̔z��
     */
    public static LANGUAGE[] filterLanguages(LanguageFilter filter) {
        final LANGUAGE[] allLanguages = LANGUAGE.values();
        final Set<LANGUAGE> resultSet = new LinkedHashSet<LANGUAGE>();

        for (final LANGUAGE language : allLanguages) {
            if (filter.accept(language)) {
                resultSet.add(language);
            }
        }

        final LANGUAGE[] resultArray = new LANGUAGE[resultSet.size()];
        return resultSet.toArray(resultArray);
    }


    /**
     * �I�u�W�F�N�g�w���Ȍ���݂̂��擾.
     * 
     * @return �I�u�W�F�N�g�w���Ȍ���̔z��
     */
    public static LANGUAGE[] getObjectOrientedLanguages() {
        return filterLanguages(new LanguageFilter() {
            public boolean accept(LANGUAGE language) {
                return language.isObjectOrientedLanguage();
            }
        });
    }
}
