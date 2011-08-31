package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.PrimitiveTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VoidTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * �g�ݍ��݌^��\���g�[�N���N���X.
 * 
 * @author kou-tngt
 *
 */
public class BuiltinTypeToken extends AstTokenAdapter {

    /**
     * bool�^��\���萔�C���X�^���X.
     */
    public static final BuiltinTypeToken BOOLEAN = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.BOOLEAN), PrimitiveTypeInfo.BOOLEAN_STRING);

    /**
     * byte�^��\���萔�C���X�^���X.
     */
    public static final BuiltinTypeToken BYTE = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.BYTE), PrimitiveTypeInfo.BYTE_STRING);

    /**
     * char�^��\���萔�C���X�^���X.
     */
    public static final BuiltinTypeToken CHAR = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.CHAR), PrimitiveTypeInfo.CHAR_STRING);

    /**
     * short�^��\���萔�C���X�^���X.
     */
    public static final BuiltinTypeToken SHORT = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.SHORT), PrimitiveTypeInfo.SHORT_STRING);

    /**
     * int�^��\���萔�C���X�^���X.
     */
    public static final BuiltinTypeToken INT = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.INT), PrimitiveTypeInfo.INT_STRING);

    /**
     * long�^��\���萔�C���X�^���X.
     */
    public static final BuiltinTypeToken LONG = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.LONG), PrimitiveTypeInfo.LONG_STRING);

    /**
     * float�^��\���萔�C���X�^���X.
     */
    public static final BuiltinTypeToken FLOAT = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.FLOAT), PrimitiveTypeInfo.FLOAT_STRING);

    /**
     * double�^��\���萔�C���X�^���X.
     */
    public static final BuiltinTypeToken DOUBLE = new BuiltinTypeToken(PrimitiveTypeInfo
            .getType(PrimitiveTypeInfo.TYPE.DOUBLE), PrimitiveTypeInfo.DOUBLE_STRING);

    /**
     * void�^��\���萔�C���X�^���X.
     */
    public static final BuiltinTypeToken VOID = new BuiltinTypeToken(VoidTypeInfo.getInstance(),
            VoidTypeInfo.VOID_STRING);

    public UnresolvedTypeInfo<? extends TypeInfo> getType() {
        return this.type;
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter#isPrimitiveType()
     */
    @Override
    public boolean isPrimitiveType() {
        return !this.isVoidType();
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenAdapter#isVoidType()
     */
    @Override
    public boolean isVoidType() {
        return this.equals(VOID);
    }

    /**
     * �w�肳�ꂽ������ŕ\������{�^��\���g�[�N�����쐬����R���X�g���N�^.
     * 
     * @param text�@���̑g�ݍ��݌^��\��������
     */
    protected BuiltinTypeToken(final UnresolvedTypeInfo<? extends TypeInfo> type, final String name) {
        super(name);

        this.type = type;
    }

    /**
     * ���̃g�[�N�����\����{�^
     */
    private final UnresolvedTypeInfo<? extends TypeInfo> type;
}
