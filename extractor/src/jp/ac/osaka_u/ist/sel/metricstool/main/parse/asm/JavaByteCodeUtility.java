package jp.ac.osaka_u.ist.sel.metricstool.main.parse.asm;


import java.util.ArrayList;
import java.util.List;


public class JavaByteCodeUtility {

    /**
     * �����ŗ^����ꂽFull Qualified Name��\��������𕪊����āC��z��Ƃ��ĕԂ�
     * �^���������Ă��Ă��C��菜�����̏����͂��Ȃ�
     * 
     * @param name
     * @return
     */
    public static String[] separateName(final String name) {

        final List<String> names = new ArrayList<String>();

        int startIndex = 0;
        int nestLevel = 0;

        for (int index = 0; index < name.length(); index++) {

            if ('<' == name.charAt(index)) {
                nestLevel++;
            }

            else if ('>' == name.charAt(index)) {
                nestLevel--;
            }

            else if ((0 == nestLevel)
                    && (('/' == name.charAt(index)) || ('$' == name.charAt(index)) || ('.' == name
                            .charAt(index)))) {
                names.add(name.substring(startIndex, index));
                startIndex = index + 1;
            }
        }
        names.add(name.substring(startIndex, name.length()));

        return names.toArray(new String[0]);
    }

    /**
     * �^����ꂽ�^�i���O�j����^��������菜�������̂�Ԃ�
     * �^�p�����[�^���Ȃ��ꍇ�͂��̂܂ܕԂ�
     * 
     * @param type
     * @return
     */
    public static String removeTypeArguments(final String type) {
        final int index = type.indexOf('<');
        return -1 == index ? type : type.substring(0, index);
    }

    /**
     * �^����ꂽ�^�i���O�j����^���������𒊏o���ĕԂ�
     * �^�p�����[�^���Ȃ��ꍇ��null��Ԃ�
     * 
     * @param type
     * @return
     */
    public static String extractTypeArguments(final String type) {
        final int openIndex = type.indexOf('<');
        final int closeIndex = type.lastIndexOf('>');
        return (-1 == openIndex) || (-1 == closeIndex) ? null : type.substring(openIndex + 1,
                closeIndex);
    }

    /**
     * �����Ƃ��ė^����ꂽ�^�̕����񂩂�C�e�^��؂�o���Ĕz��Ƃ��ĕԂ�
     * 
     * @param text
     * @return
     */
    public static String[] separateTypes(final String text) {

        if (null == text) {
            throw new IllegalArgumentException();
        }

        final List<String> types = new ArrayList<String>();

        for (int index = 0, nestLevel = 0, dimension = 0; index < text.length(); index++) {

            if ('<' == text.charAt(index)) {
                nestLevel++;
            }

            else if ('>' == text.charAt(index)) {
                nestLevel--;
            }

            // �X���[������O�̂��߂̕���C���ɏ����͂Ȃ�
            else if ('^' == text.charAt(index)) {

            }

            //�ꕶ���̌^�̂Ƃ�
            else if ((0 == nestLevel) && isSingleCharacterType(text.charAt(index))) {
                final String type = String.valueOf(text.charAt(index));
                final StringBuilder sb = new StringBuilder();
                for (int i = 1; i <= dimension; i++) { //�z����l��
                    sb.append('[');
                }
                sb.append(type);
                types.add(sb.toString());
                dimension = 0;
            }

            // ���������̌^�̂Ƃ�
            else if ((0 == nestLevel) && isMultipleCharactersType(text.charAt(index))) {
                final String type = extractMultipleCharactersType(text.substring(index));
                final StringBuilder sb = new StringBuilder();
                for (int i = 1; i <= dimension; i++) { //�z����l��
                    sb.append('[');
                }
                sb.append(type);
                types.add(sb.toString());
                index += type.length() - 1;
                dimension = 0;
            }

            else if ((0 == nestLevel) && ('[' == text.charAt(index))) {
                dimension++;
            }

            // ����ȊO�̂Ƃ��͏�Ԉُ�
            else
                throw new IllegalStateException();
        }

        return types.toArray(new String[0]);
    }

    /**
     * �����ŗ^����ꂽ�������ꕶ���^��\���ꍇ��true,�����łȂ��ꍇ��false��Ԃ�
     * 
     * @param c
     * @return
     */
    private static boolean isSingleCharacterType(final char c) {

        switch (c) {
        case 'Z':
        case 'C':
        case 'B':
        case 'S':
        case 'I':
        case 'F':
        case 'J':
        case 'D':
        case 'V':
        case '*':
            return true;
        default:
            return false;
        }
    }

    /**
     * �����ŗ^����ꂽ���������������^��\���ꍇ��true,�����łȂ��ꍇ��false��Ԃ�
     * 
     * @param c
     * @return
     */
    private static boolean isMultipleCharactersType(final char c) {

        switch (c) {
        case 'L':
        case 'T':
        case '+':
        case '-':
            return true;
        default:
            return false;
        }
    }

    /**
     * �����Ƃ��ė^����ꂽ������̐擪�Ɍ���镡���^��؂�o���ĕԂ�
     * 
     * @param text
     * @return
     */
    private static String extractMultipleCharactersType(final String text) {

        for (int index = 0, nestLevel = 0; index < text.length(); index++) {

            if ('<' == text.charAt(index)) {
                nestLevel++;
            }

            else if ('>' == text.charAt(index)) {
                nestLevel--;
            }

            else if ((';' == text.charAt(index)) && (0 == nestLevel)) {
                return text.substring(0, index + 1);
            }
        }

        assert false : "Here shouldn't be reached!";
        return null;
    }
}
