package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression;


import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedTypeInfo;


/**
 * �^������\�����v�f
 * 
 * @author kou-tngt
 *
 */
public class TypeArgumentElement extends ExpressionElement {

    /**
     * ����type���^�����Ƃ��ĕ\���C���X�^���X���쐬����D
     * 
     * @param type
     */
    public TypeArgumentElement(UnresolvedTypeInfo<? extends TypeInfo> type) {
        this.type = type;
    }

    /**
     * �^������Ԃ��D
     * @return ���̃C���X�^���X���\���^����
     */
    public UnresolvedTypeInfo<? extends TypeInfo> getType() {
        return type;
    }

    /**
     * ���̃C���X�^���X���\���^����
     */
    private final UnresolvedTypeInfo<? extends TypeInfo> type;
}
