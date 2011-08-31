package jp.ac.osaka_u.ist.sel.metricstool.main.data.target;


import java.util.HashMap;
import java.util.Map;

import jp.ac.osaka_u.ist.sel.metricstool.main.security.MetricsToolSecurityManager;


/**
 * �z��^��\�����߂̃N���X�D
 * 
 * @author higo
 * 
 */
@SuppressWarnings("serial")
public class ArrayTypeInfo implements ReferenceTypeInfo {

    /**
     * �^����Ԃ�
     * 
     * @return �^��
     */
    @Override
    public String getTypeName() {
        final TypeInfo elementType = this.getElementType();
        final int dimension = this.getDimension();

        final StringBuffer buffer = new StringBuffer();
        buffer.append(elementType.getTypeName());
        for (int i = 0; i < dimension; i++) {
            buffer.append("[]");
        }
        return buffer.toString();
    }

    /**
     * ���������ǂ����̃`�F�b�N���s��
     * 
     * @param typeInfo ��r�Ώی^
     * @return �������ꍇ��true, �����łȂ��ꍇ��false
     */
    @Override
    public boolean equals(final TypeInfo typeInfo) {

        if (null == typeInfo) {
            throw new NullPointerException();
        }

        if (!(typeInfo instanceof ArrayTypeInfo)) {
            return false;
        }

        final TypeInfo element = this.getElementType();
        final TypeInfo correspondElement = ((ArrayTypeInfo) typeInfo).getElementType();
        if (!element.equals(correspondElement)) {
            return false;
        }

        final int dimension = this.getDimension();
        final int correspondDimension = ((ArrayTypeInfo) typeInfo).getDimension();
        return dimension == correspondDimension;
    }

    /**
     * �z��̗v�f��Ԃ�
     * 
     * @return �z��̗v�f�̌^
     */
    public final TypeInfo getElementType() {
        return this.element;
    }

    /**
     * �z��̎�����Ԃ�
     * 
     * @return �z��̎���
     */
    public final int getDimension() {
        return this.dimension;
    }

    /**
     * ArrayTypeInfo �̃C���X�^���X��Ԃ����߂̃t�@�N�g�����\�b�h�D
     * 
     * @param element �^��\���ϐ�
     * @param dimension ������\���ϐ�
     * @return �������� ArrayTypeInfo �I�u�W�F�N�g
     */
    public static ArrayTypeInfo getType(final TypeInfo element, final int dimension) {

        if (null == element) {
            throw new NullPointerException();
        }
        if (dimension < 1) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }

        Key key = new Key(element, dimension);
        ArrayTypeInfo arrayType = ARRAY_TYPE_MAP.get(key);
        if (arrayType == null) {
            arrayType = new ArrayTypeInfo(element, dimension);
            ARRAY_TYPE_MAP.put(key, arrayType);
        }

        return arrayType;
    }

    /**
     * �I�u�W�F�N�g�̏��������s���D�z��̗v�f�̌^�Ɣz��̎������^�����Ȃ���΂Ȃ�Ȃ�
     * 
     * @param element �z��̗v�f
     * @param dimension �z��̎���
     */
    ArrayTypeInfo(final TypeInfo element, final int dimension) {

        MetricsToolSecurityManager.getInstance().checkAccess();
        if (null == element) {
            throw new NullPointerException();
        }
        if (1 > dimension) {
            throw new IllegalArgumentException("Array dimension must be 1 or more!");
        }

        this.element = element;
        this.dimension = dimension;
    }

    /**
     * �z��̗v�f��ۑ�����ϐ�
     */
    private final TypeInfo element;

    /**
     * �z��̎�����ۑ�����ϐ�
     */
    private final int dimension;

    /**
     * ArrayTypeInfo �I�u�W�F�N�g���ꌳ�Ǘ����邽�߂� Map�D�I�u�W�F�N�g�̓t�@�N�g�����\�b�h�Ő��������D
     */
    private static final Map<Key, ArrayTypeInfo> ARRAY_TYPE_MAP = new HashMap<Key, ArrayTypeInfo>();

    /**
     * �ϐ��̌^�Ǝ�����p���ăL�[�ƂȂ�N���X�D
     * 
     * @author higo
     */
    static class Key {

        /**
         * ���L�[
         */
        private final TypeInfo element;

        /**
         * ���L�[
         */
        private final int dimension;

        /**
         * ���C���L�[����C�L�[�I�u�W�F�N�g�𐶐�����
         * 
         * @param element ���L�[
         * @param dimension ���L�[
         */
        Key(final TypeInfo element, final int dimension) {

            if (null == element) {
                throw new NullPointerException();
            }
            if (1 > dimension) {
                throw new IllegalArgumentException("Array dimension must be 1 or more!");
            }

            this.element = element;
            this.dimension = dimension;
        }

        /**
         * ���̃I�u�W�F�N�g�̃n�b�V���R�[�h��Ԃ��D
         */
        @Override
        public int hashCode() {
            return this.element.hashCode() + this.dimension;
        }

        /**
         * ���̃L�[�I�u�W�F�N�g�̑��L�[��Ԃ��D
         * 
         * @return ���L�[
         */
        public String getFirstKey() {
            return this.element.getTypeName();
        }

        /**
         * ���̃L�[�I�u�W�F�N�g�̑��L�[��Ԃ��D
         * 
         * @return ���L�[
         */
        public int getSecondKey() {
            return this.dimension;
        }

        /**
         * ���̃I�u�W�F�N�g�ƈ����Ŏw�肳�ꂽ�I�u�W�F�N�g������������Ԃ��D
         */
        @Override
        public boolean equals(Object o) {

            if (null == o) {
                throw new NullPointerException();
            }

            if (!(o instanceof Key)) {
                return false;
            }

            final String firstKey = this.getFirstKey();
            final String correspondFirstKey = ((Key) o).getFirstKey();
            if (!firstKey.equals(correspondFirstKey)) {
                return false;
            }

            final int secondKey = this.getSecondKey();
            final int correspondSecondKey = ((Key) o).getSecondKey();
            return secondKey == correspondSecondKey;
        }
    }
}
