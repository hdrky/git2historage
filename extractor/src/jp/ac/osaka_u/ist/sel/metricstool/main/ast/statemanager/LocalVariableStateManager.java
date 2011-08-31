package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * AST�r�W�^�[�����[�J���ϐ���`���ɓ��B�������ɏ�ԑJ�ڂ���X�e�[�g�}�l�[�W��
 * 
 * @author kou-tngt
 *
 */
public class LocalVariableStateManager extends VariableDefinitionStateManager {

    /**
     * �g�[�N�������[�J���ϐ���`����\�����ǂ�����Ԃ��D
     * ����ɂ�token.isLocalVariableDefinition()���\�b�h��p����
     * 
     * @param token�@���[�J���ϐ���`���ǂ������肷��g�[�N��
     * @return ���[�J���ϐ���`���ł����true.
     */
    @Override
    protected boolean isDefinitionToken(final AstToken token) {
        return token.isLocalVariableDefinition();
    }
}
