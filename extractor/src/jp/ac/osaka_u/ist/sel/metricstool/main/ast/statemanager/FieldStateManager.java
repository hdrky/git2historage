package jp.ac.osaka_u.ist.sel.metricstool.main.ast.statemanager;


import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * AST�r�W�^�[���t�B�[���h��`���ɓ��B�������ɏ�ԑJ�ڂ���X�e�[�g�}�l�[�W��
 * 
 * @author kou-tngt
 *
 */
public class FieldStateManager extends VariableDefinitionStateManager {

    /**
     * �����̃g�[�N�����t�B�[���h��`�����ǂ�����Ԃ��D
     * token.isFieldDefinition()���\�b�h��p���Ĕ��肷��D
     * 
     * @param token �t�B�[���h��`�����ǂ����𒲂ׂ�g�[�N��
     * @return token���t�B�[���h��`���Ȃ�true
     */
    @Override
    protected boolean isDefinitionToken(final AstToken token) {
        return token.isFieldDefinition();
    }

}
