package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * AST�r�W�^�[�����\�b�h�p�����[�^��`���ɓ��B�������ɏ�ԑJ�ڂ���X�e�[�g�}�l�[�W��
 * 
 * @author kou-tngt
 *
 */
public class MethodParameterStateManager extends VariableDefinitionStateManager {

    /**
     * �g�[�N�������\�b�h�p�����[�^��`����\�����ǂ�����Ԃ��D
     * ����ɂ�token.isMethodParameterDefinition���\�b�h��p����
     * 
     * @param token�@���\�b�h�p�����[�^��`���ǂ������肷��g�[�N��
     * @return ���\�b�h�p�����[�^��`���ł����true.
     */
    @Override
    protected boolean isDefinitionToken(final AstToken token) {
        return token.isMethodParameterDefinition();
    }

}
