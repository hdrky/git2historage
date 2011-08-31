package jp.ac.osaka_u.ist.sel.metricstool.main.ast.java;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.BuildDataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.ExpressionElementManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.expression.TypeElementBuilder;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReferenceTypeInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.unresolved.UnresolvedReferenceTypeInfo;


/**
 * Java�̎����ɓo�ꂷ��^�v�f���\�z����N���X�D
 * 
 * �e�N���X�� {@link TypeElementBuilder#getTypeUpperBounds()} ���\�b�h���I�[�o�[���C�h���C
 * null���A���Ă����ꍇ��java.lang.Object��Ԃ��悤�Ɋg������D
 * 
 * @author kou-tngt
 *
 */
public class JavaTypeElementBuilder extends TypeElementBuilder {

    public JavaTypeElementBuilder(ExpressionElementManager expressionManager,
            BuildDataManager buildManager) {
        super(expressionManager, buildManager);
    }

    @Override
    protected UnresolvedReferenceTypeInfo<? extends ReferenceTypeInfo> getDefaultTypeUpperBound() {
        return JavaTypeBuilder.JAVA_LANG_OBJECT;
    }
}
