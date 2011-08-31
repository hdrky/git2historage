package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * �v���~�e�B�u�^��\�����߂̃N���X�D�v���~�e�B�u�^�̓v���O���~���O����ɂ���Ē񏥂���Ă���^�ł��邽�߁C ���[�U���V���炵���^����邱�Ƃ��ł��Ȃ��悤�C�R���X�g���N�^�� private
 * �ɂ��Ă���D
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public class PrimitiveTypeInfo implements TypeInfo, UnresolvedTypeInfo<PrimitiveTypeInfo> {

    public static boolean isJavaWrapperType(final TypeInfo type) {

        // �N���X�Q�ƌ^�łȂ��ꍇ��false
        if (!(type instanceof ClassTypeInfo)) {
            return false;
        }

        final ClassTypeInfo classType = (ClassTypeInfo) type;
        final ClassInfo referencedClass = classType.getReferencedClass();

        final ClassInfo booleanClass = DataManager.getInstance().getClassInfoManager()
                .getClassInfo(new String[] { "java", "lang", "Boolean" });
        final ClassInfo byteClass = DataManager.getInstance().getClassInfoManager().getClassInfo(
                new String[] { "java", "lang", "Byte" });
        final ClassInfo characterClass = DataManager.getInstance().getClassInfoManager()
                .getClassInfo(new String[] { "java", "lang", "Character" });
        final ClassInfo doubleClass = DataManager.getInstance().getClassInfoManager().getClassInfo(
                new String[] { "java", "lang", "Double" });
        final ClassInfo floatClass = DataManager.getInstance().getClassInfoManager().getClassInfo(
                new String[] { "java", "lang", "Float" });
        final ClassInfo integerClass = DataManager.getInstance().getClassInfoManager()
                .getClassInfo(new String[] { "java", "lang", "Integer" });
        final ClassInfo longClass = DataManager.getInstance().getClassInfoManager().getClassInfo(
                new String[] { "java", "lang", "Long" });
        final ClassInfo shortClass = DataManager.getInstance().getClassInfoManager().getClassInfo(
                new String[] { "java", "lang", "Short" });

        return referencedClass.equals(booleanClass) || referencedClass.equals(byteClass)
                || referencedClass.equals(characterClass) || referencedClass.equals(doubleClass)
                || referencedClass.equals(floatClass) || referencedClass.equals(integerClass)
                || referencedClass.equals(longClass) || referencedClass.equals(shortClass);
    }

    /**
     * �����ŗ^����ꂽ�N���X�^�ƑΉ�����v���~�e�B�u�^��Ԃ�.
     * �^����ꂽ�N���X�^���s���Ȃ��̂ł���ꍇ�́Cnull��Ԃ��D
     * 
     * @param classType
     * @return
     */
    public static PrimitiveTypeInfo getPrimitiveType(final ClassTypeInfo classType) {

        final ClassInfo referencedClass = classType.getReferencedClass();

        final ClassInfo booleanClass = DataManager.getInstance().getClassInfoManager()
                .getClassInfo(new String[] { "java", "lang", "Boolean" });
        final ClassInfo byteClass = DataManager.getInstance().getClassInfoManager().getClassInfo(
                new String[] { "java", "lang", "Byte" });
        final ClassInfo characterClass = DataManager.getInstance().getClassInfoManager()
                .getClassInfo(new String[] { "java", "lang", "Character" });
        final ClassInfo doubleClass = DataManager.getInstance().getClassInfoManager().getClassInfo(
                new String[] { "java", "lang", "Double" });
        final ClassInfo floatClass = DataManager.getInstance().getClassInfoManager().getClassInfo(
                new String[] { "java", "lang", "Float" });
        final ClassInfo integerClass = DataManager.getInstance().getClassInfoManager()
                .getClassInfo(new String[] { "java", "lang", "Integer" });
        final ClassInfo longClass = DataManager.getInstance().getClassInfoManager().getClassInfo(
                new String[] { "java", "lang", "Long" });
        final ClassInfo shortClass = DataManager.getInstance().getClassInfoManager().getClassInfo(
                new String[] { "java", "lang", "Short" });

        if (referencedClass.equals(booleanClass)) {
            return BOOLEAN;
        } else if (referencedClass.equals(byteClass)) {
            return BYTE;
        } else if (referencedClass.equals(characterClass)) {
            return CHAR;
        } else if (referencedClass.equals(doubleClass)) {
            return DOUBLE;
        } else if (referencedClass.equals(floatClass)) {
            return FLOAT;
        } else if (referencedClass.equals(integerClass)) {
            return INT;
        } else if (referencedClass.equals(longClass)) {
            return LONG;
        } else if (referencedClass.equals(shortClass)) {
            return SHORT;
        } else {
            return null;
        }
    }

    /**
     * �v���~�e�B�u�^�̊e�v�f��\�����߂̗񋓌^
     * 
     * @author higo
     * 
     */
    public enum TYPE {

        /**
         * �u�[���^��\��
         */
        BOOLEAN {

            @Override
            public String getName() {
                return "boolean";
            }
        },

        /**
         * BYTE�^��\��
         */
        BYTE {
            @Override
            public String getName() {
                return "byte";
            }
        },

        /**
         * CHAR�^��\��
         */
        CHAR {
            @Override
            public String getName() {
                return "char";
            }
        },

        /**
         * SHORT��\��
         */
        SHORT {
            @Override
            public String getName() {
                return "short";
            }
        },

        /**
         * INT��\��
         */
        INT {
            @Override
            public String getName() {
                return "int";
            }
        },

        /**
         * LONG�^��\��
         */
        LONG {
            @Override
            public String getName() {
                return "long";
            }
        },

        /**
         * FLOAT�^��\��
         */
        FLOAT {
            @Override
            public String getName() {
                return "float";
            }
        },

        /**
         * DOUBLE�^��\��
         */
        DOUBLE {
            @Override
            public String getName() {
                return "double";
            }
        };

        /**
         * �^����Ԃ�
         * 
         * @return �^��
         */
        public abstract String getName();
    }

    /**
     * boolean ��\���萔
     */
    public static final String BOOLEAN_STRING = TYPE.BOOLEAN.getName();

    /**
     * byte ��\���萔
     */
    public static final String BYTE_STRING = TYPE.BYTE.getName();

    /**
     * char ��\���萔
     */
    public static final String CHAR_STRING = TYPE.CHAR.getName();

    /**
     * short ��\���萔
     */
    public static final String SHORT_STRING = TYPE.SHORT.getName();

    /**
     * int ��\���萔
     */
    public static final String INT_STRING = TYPE.INT.getName();

    /**
     * long ��\���萔
     */
    public static final String LONG_STRING = TYPE.LONG.getName();

    /**
     * float ��\���萔
     */
    public static final String FLOAT_STRING = TYPE.FLOAT.getName();

    /**
     * double ��\���萔
     */
    public static final String DOUBLE_STRING = TYPE.DOUBLE.getName();

    /**
     * boolean �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo BOOLEAN = new PrimitiveTypeInfo(TYPE.BOOLEAN);

    /**
     * byte �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo BYTE = new PrimitiveTypeInfo(TYPE.BYTE);

    /**
     * char �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo CHAR = new PrimitiveTypeInfo(TYPE.CHAR);

    /**
     * short �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo SHORT = new PrimitiveTypeInfo(TYPE.SHORT);

    /**
     * int �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo INT = new PrimitiveTypeInfo(TYPE.INT);

    /**
     * long �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo LONG = new PrimitiveTypeInfo(TYPE.LONG);

    /**
     * float �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo FLOAT = new PrimitiveTypeInfo(TYPE.FLOAT);

    /**
     * double �^��\�����߂̒萔�D
     */
    public static final PrimitiveTypeInfo DOUBLE = new PrimitiveTypeInfo(TYPE.DOUBLE);

    /**
     * {@link PrimitiveTypeInfo}�̃t�@�N�g�����\�b�h�D
     * 
     * @param type �쐬����^�̗񋓌^
     * @return �w�肳�ꂽ����\�� {@link PrimitiveTypeInfo} �̃C���X�^���X�D
     */
    public static PrimitiveTypeInfo getType(final TYPE type) {

        if (null == type) {
            throw new NullPointerException();
        }

        switch (type) {
        case BOOLEAN:
            return BOOLEAN;
        case BYTE:
            return BYTE;
        case CHAR:
            return CHAR;
        case DOUBLE:
            return DOUBLE;
        case FLOAT:
            return FLOAT;
        case INT:
            return INT;
        case LONG:
            return LONG;
        case SHORT:
            return SHORT;
        default:
            throw new IllegalArgumentException();
        }
    }

    /**
     * ���ɉ����ς݂��ǂ�����Ԃ�
     * 
     * @return ��� true ���Ԃ����
     */
    public boolean alreadyResolved() {
        return true;
    }

    /**
     * �^�������ꂽ����Ԃ�
     * 
     * @return �������g��Ԃ�
     */
    public PrimitiveTypeInfo getResolved() {
        return this;
    }

    /**
     * ���O�������s��
     * 
     * @param usingClass ���O�������s���G���e�B�e�B������N���X
     * @param usingMethod ���O�������s���G���e�B�e�B�����郁�\�b�h
     * @param classInfoManager �p����N���X�}�l�[�W��
     * @param fieldInfoManager �p����t�B�[���h�}�l�[�W��
     * @param methodInfoManager �p���郁�\�b�h�}�l�[�W��
     * 
     * @return �����ς݂̌^�i�������g�j
     */
    public PrimitiveTypeInfo resolve(final TargetClassInfo usingClass,
            final CallableUnitInfo usingMethod, final ClassInfoManager classInfoManager,
            final FieldInfoManager fieldInfoManager, final MethodInfoManager methodInfoManager) {
        return this;
    }

    /**
     * ���̌^�̃v���~�e�B�u�^��Ԃ�
     * 
     * @return ���̌^�̃v���~�e�B�u�^
     */
    public TYPE getPrimitiveType() {
        return this.type;
    }

    /**
     * �^����Ԃ��D
     * 
     * @return �^��
     */
    @Override
    public String getTypeName() {
        return this.type.getName();
    }

    /**
     * �I�u�W�F�N�g�̓������̃`�F�b�N���s��
     */
    @Override
    public boolean equals(final TypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        if (!(typeInfo instanceof PrimitiveTypeInfo)) {
            return false;
        }

        return this.getTypeName().equals(typeInfo.getTypeName());
    }

    /**
     * �I�u�W�F�N�g�Ɍ^��^���ď���������D �^���͌Œ�ł��邽�߁C�O������̓I�u�W�F�N�g�𐶐��ł��Ȃ��悤�ɂ��Ă���D
     * 
     * @param type �^
     */
    private PrimitiveTypeInfo(final TYPE type) {

        if (null == type) {
            throw new NullPointerException();
        }

        this.type = type;
    }

    /**
     * �^��\���ϐ��D
     */
    private final TYPE type;
}
