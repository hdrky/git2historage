package jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.statement;


import java.util.ArrayList;
import java.util.List;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.DataBuilderAdapter;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.ConstantToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;


/**
 * �A�m�e�[�V�����ɗ^�������������͂���r���_�[
 * ���݃A�m�e�[�V�����̈����͐^�ʖڂɉ�͂��Ă��Ȃ�
 * �^����ꂽ�����͂��ׂ�String�Ƃ��Ď擾���邱�Ƃɂ��Ă���
 * @author a-saitoh
 *
 */
public class AnnotationStatementBuilder extends DataBuilderAdapter<String> {

    @Override
    public void entered(AstVisitEvent e) {
        if (isActive()) {

            //ANNOTATION_STRING�̌�ɂ��镶������擾
            if (!e.getToken().isAnnotationString()) {
                this.annotationArguments.append(e.getText());
            }
        }
    }

    @Override
    public void exited(AstVisitEvent e) throws ASTParseException {
        //  do nothing
    }

    /**
     * �\�z����String�f�[�^�𓾂�D
     * �^�ʖڂɉ�͂���Ȃ��Stack�Ɋi�[����K�v�����邩��(�A�m�e�[�V�����͓���q�ɂł���̂�)
     * @return
     */
    public String getArguments() {
        return this.annotationArguments.toString();
    }

    public void clearAnnotationArguments() {
        this.annotationArguments.delete(0, this.annotationArguments.length());
    }

    private final StringBuffer annotationArguments = new StringBuffer();

}
