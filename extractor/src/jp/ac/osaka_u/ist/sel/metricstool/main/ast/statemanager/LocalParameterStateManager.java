package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * AST�r�W�^�[�����\�b�h�����̃p�����[�^��`���ifor�����̕ϐ���`�Ȃǂ̂悤�ɁC��`���ꂽ�ꏊ���玟�̃u���b�N�̏I���܂ŗL���ȕϐ���`���j
 * �ɓ��B�������ɏ�ԑJ�ڂ���X�e�[�g�}�l�[�W��
 * 
 * @author kou-tngt
 *
 */
public class LocalParameterStateManager extends VariableDefinitionStateManager {

    /**
     * �g�[�N�������[�J���p�����[�^��`�����ǂ�����Ԃ��D
     * ����ɂ�token.isLocalParameterDefinition()���\�b�h��p����
     * 
     * @param token�@���[�J���p�����[�^��`���ǂ������肷��g�[�N��
     * @return ���[�J���p�����[�^��`���ł����true.
     */
    @Override
    protected boolean isDefinitionToken(AstToken token) {
        return token.isLocalParameterDefinition();
    }
}