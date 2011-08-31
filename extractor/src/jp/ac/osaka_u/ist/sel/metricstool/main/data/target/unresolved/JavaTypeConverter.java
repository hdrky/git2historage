package jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;


/**
 * Java����p�̌^�ϊ����[�e�B���e�B
 * 
 * @author higo
 */
final public class JavaTypeConverter extends TypeConverter {

    static final JavaTypeConverter SINGLETON = new JavaTypeConverter();

    /**
     * �v���~�e�B�u�^�̃��b�p�[�N���X��Ԃ�
     * 
     * @param primitiveType ���b�p�[�N���X���擾�������v���~�e�B�u�^
     * @return �w�肵���v���~�e�B�u�^�̃��b�p�[�N���X
     */
    @Override
    public ClassInfo getWrapperClass(final PrimitiveTypeInfo primitiveType) {

        if (null == primitiveType) {
            throw new NullPointerException();
        }

        final ClassInfoManager classInfoManager = DataManager.getInstance().getClassInfoManager();

        switch (primitiveType.getPrimitiveType()) {
        case BOOLEAN:
            final ClassInfo booleanClass = classInfoManager.getClassInfo(new String[] { "java",
                    "lang", "Boolean" });
            return booleanClass;
        case BYTE:
            final ClassInfo byteClass = classInfoManager.getClassInfo(new String[] { "java",
                    "lang", "Byte" });
            return byteClass;
        case CHAR:
            final ClassInfo charClass = classInfoManager.getClassInfo(new String[] { "java",
                    "lang", "Character" });
            return charClass;
        case DOUBLE:
            final ClassInfo doubleClass = classInfoManager.getClassInfo(new String[] { "java",
                    "lang", "Double" });
            return doubleClass;
        case FLOAT:
            final ClassInfo floatClass = classInfoManager.getClassInfo(new String[] { "java",
                    "lang", "Float" });
            return floatClass;
        case INT:
            final ClassInfo intClass = classInfoManager.getClassInfo(new String[] { "java", "lang",
                    "Integer" });
            return intClass;
        case LONG:
            final ClassInfo longClass = classInfoManager.getClassInfo(new String[] { "java",
                    "lang", "Long" });
            return longClass;
        case SHORT:
            final ClassInfo shortClass = classInfoManager.getClassInfo(new String[] { "java",
                    "lang", "Short" });
            return shortClass;
        default:
            throw new IllegalArgumentException();
        }
    }
}
