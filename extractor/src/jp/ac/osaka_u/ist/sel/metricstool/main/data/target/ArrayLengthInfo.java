package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


/**
 * �z��̒�����\���ϐ� length ��\���N���X
 * 
 * @author higo
 *
 */
@SuppressWarnings("serial")
public final class ArrayLengthInfo extends FieldInfo {

    /**
     * ���̃I�u�W�F�N�g��(�֋X��)��`���Ă���z��I�u�W�F�N�g��^���ď�����
     * 
     * @param ownerArray �z��I�u�W�F�N�g
     */
    private ArrayLengthInfo(final ArrayTypeInfo ownerArray) {

        super(new HashSet<ModifierInfo>(), "length", new ArrayTypeClassInfo(ownerArray), true, 0,
                0, 0, 0);
        this.setType(PrimitiveTypeInfo.INT);
    }

    /**
     * ArrayLengthInfo�@�𓾂邽�߂̃t�@�N�g�����\�b�h
     * 
     * @param ownerArray
     * @return
     */
    public static ArrayLengthInfo getArrayLengthInfo(final ArrayTypeInfo ownerArray) {

        if (null == ownerArray) {
            throw new IllegalArgumentException();
        }

        ArrayLengthInfo arrayLength = arrayMap.get(ownerArray);
        if (null == arrayLength) {
            arrayLength = new ArrayLengthInfo(ownerArray);
            arrayMap.put(ownerArray, arrayLength);
        }
        return arrayLength;
    }

    private final static Map<ArrayTypeInfo, ArrayLengthInfo> arrayMap = new HashMap<ArrayTypeInfo, ArrayLengthInfo>();
}
