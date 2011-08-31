package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.*;


/**
 * �A�m�e�[�V�����̏���\���N���X
 * �A�m�e�[�V�����͏C���q�̈��Ƃ��Ĉ���
 * ���݂̓A�m�e�[�V�����ɗ^����ꂽ�����͂��ׂĕ�����(String)�Ƃ��Ă�����͂��Ă��Ȃ�
 * @author a-saitoh
 *
 */

@SuppressWarnings("serial")
public class AnnotationInfo extends JavaModifierInfo {

    public AnnotationInfo(final String name, final String arguments) {
        this.name = name;
        this.annotationArgument = arguments;
    }

    public String getAnnotationArgument() {
        return annotationArgument;
    }

    private String annotationArgument;

    
}
