package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashSet;
import java.util.Set;


/**
 * �z��^�̒�`��\�����߂̃N���X�D
 * �ł���Ύg�������͂Ȃ��D
 * 
 * @author higo
 *
 */
@SuppressWarnings( { "serial" })
public final class ArrayTypeClassInfo extends ClassInfo {

    /**
     * �z��̌^��^���āC�I�u�W�F�N�g��������
     * 
     * @param arrayType �z��̌^
     */
    public ArrayTypeClassInfo(final ArrayTypeInfo arrayType) {

        super(new HashSet<ModifierInfo>(), NamespaceInfo.UNKNOWN, NONAME, false, 0, 0, 0, 0);

        if (null == arrayType) {
            throw new IllegalArgumentException();
        }
        this.arrayType = arrayType;
    }

    /**
     * �z��̌^��Ԃ�
     * 
     * @return �z��̌^
     */
    public ArrayTypeInfo getArrayType() {
        return this.arrayType;
    }

    /**
     * �ϐ����p�̈ꗗ��Ԃ��D
     * �ǂ̕ϐ����p�����Ă��Ȃ��̂ŁC���set���Ԃ����
     * 
     * @return �ϐ����p��Set
     */
    @Override
    public Set<VariableUsageInfo<? extends VariableInfo<? extends UnitInfo>>> getVariableUsages() {
        return VariableUsageInfo.EmptySet;
    }

    /**
     * ��`���ꂽ�ϐ���Set��Ԃ�
     * 
     * @return ��`���ꂽ�ϐ���Set
     */
    @Override
    public Set<VariableInfo<? extends UnitInfo>> getDefinedVariables() {
        return VariableInfo.EmptySet;
    }

    /**
     * �Ăяo����Set��Ԃ�
     * 
     * @return �Ăяo����Set
     */
    @Override
    public Set<CallInfo<? extends CallableUnitInfo>> getCalls() {
        return CallInfo.EmptySet;
    }

    private final ArrayTypeInfo arrayType;

    /**
     * �z��^��\�����߂̃N���X�Ȃ̂Ŗ��O�͂Ȃ��D
     * ���O���Ȃ����Ƃ�\���萔�D
     */
    public static final String NONAME = "noname";
}
