package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor;


import java.util.EventObject;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;


/**
 * {@link AstVisitor} ��AST�̔C�ӂ̃m�[�h�ɓ��B�������ɔ��s�����C�x���g.
 * 
 * @author kou-tngt
 *
 */
public class AstVisitEvent extends EventObject {

    /**
     * ���̃C�x���g���\���m�[�h�Ɋւ�����������Ɏ��R���X�g���N�^.
     * @param source�@���̃C�x���g�̔��s���ƂȂ�r�W�^�[
     * @param token ���B�����m�[�h�̎�ނ�\���g�[�N��
     * @param text ���B�����m�[�h���\��������
     * @param enterLine ���B�����m�[�h���\���v�f�̃\�[�X�R�[�h��ł̊J�n�s
     * @param enterColumn ���B�����m�[�h���\���v�f�̃\�[�X�R�[�h��̏o�J�n��
     * @param exitLine ���B�����m�[�h���\���v�f�̃\�[�X�R�[�h��ł̏I���s
     * @param exitColumn ���B�����m�[�h���\���v�f�̃\�[�X�R�[�h��ł̏I���s
     */
    public AstVisitEvent(final AstVisitor source, final AstToken token, final String text, final AstToken parentToken,
            final int enterLine, final int enterColumn, final int exitLine, final int exitColumn) {
        super(source);
        this.source = source;
        this.token = token;
        this.text = text;
        this.parentToken = parentToken;
        this.startLine = enterLine;
        this.endLine = exitLine;
        this.startColumn = enterColumn;
        this.endColumn = exitColumn;
    }

    /**
     * ���̃C�x���g�̔��s�҂ł���r�W�^�[��Ԃ�.
     */
    @Override
    public AstVisitor getSource() {
        return this.source;
    }

    /**
     * ���̃C�x���g���֘A����AST�m�[�h�̎�ނ�\���g�[�N����Ԃ�.
     * 
     * @return ���̃C�x���g���֘A����AST�m�[�h�̎�ނ�\���g�[�N��
     */
    public AstToken getToken() {
        return this.token;
    }
    
    /**
     * ���̃C�x���g���֘A����AST�m�[�h�̕������Ԃ��D
     * 
     * @return ���̃C�x���g���֘A����AST�m�[�h�̕�����
     */
    public final String getText() {
        return this.text;
    }
    
    /**
     * ���̃C�x���g���֘A����AST�m�[�h�̐e�m�[�h�̎�ނ�\���g�[�N����Ԃ��D
     * @return ���̃C�x���g���֘A����AST�m�[�h�̐e�m�[�h�̎�ނ�\���g�[�N��
     */
    public AstToken getParentToken() {
        return this.parentToken;
    }

    /**
     * ���̃C�x���g���֘A����AST�m�[�h�̃\�[�X�R�[�h��ł̊J�n���Ԃ�.
     * 
     * @return�@���̃C�x���g���֘A����AST�m�[�h�̃\�[�X�R�[�h��ł̊J�n��
     */
    public int getStartColumn() {
        return this.startColumn;
    }

    /**
     * ���̃C�x���g���֘A����AST�m�[�h�̃\�[�X�R�[�h��ł̊J�n�s��Ԃ�.
     * 
     * @return�@���̃C�x���g���֘A����AST�m�[�h�̃\�[�X�R�[�h��ł̊J�n�s
     */
    public int getStartLine() {
        return this.startLine;
    }

    /**
     * ���̃C�x���g���֘A����AST�m�[�h�̃\�[�X�R�[�h��ł̏I�����Ԃ�.
     * 
     * @return�@���̃C�x���g���֘A����AST�m�[�h�̃\�[�X�R�[�h��ł̏I����.
     */
    public int getEndColumn() {
        return this.endColumn;
    }

    /**
     * ���̃C�x���g���֘A����AST�m�[�h�̃\�[�X�R�[�h��ł̏I���s��Ԃ�.
     * 
     * @return�@���̃C�x���g���֘A����AST�m�[�h�̃\�[�X�R�[�h��ł̏I���s.
     */
    public int getEndLine() {
        return this.endLine;
    }

    /**
     * ���̃C�x���g�̔��s�҂ł���r�W�^�[��Ԃ�.
     */
    private final AstVisitor source;

    /**
     * ���̃C�x���g���֘A����AST�m�[�h�̎�ނ�\���g�[�N��
     */
    private final AstToken token;
    
    /**
     * ���̃C�x���g���֘A����AST�m�[�h�̕�����
     */
    private final String text;

    /**
     * ���̃C�x���g���֘A����AST�m�[�h�̐e�m�[�h�̎�ނ�\���g�[�N��
     */
    private final AstToken parentToken;

    /**
     * ���̃C�x���g���֘A����AST�m�[�h�̃\�[�X�R�[�h��ł̊J�n�s.
     */
    private final int startLine;

    /**
     * ���̃C�x���g���֘A����AST�m�[�h�̃\�[�X�R�[�h��ł̊J�n��.
     */
    private final int startColumn;

    /**
     * ���̃C�x���g���֘A����AST�m�[�h�̃\�[�X�R�[�h��ł̏I���s.
     */
    private final int endLine;

    /**
     * ���̃C�x���g���֘A����AST�m�[�h�̃\�[�X�R�[�h��ł̏I����.
     */
    private final int endColumn;

}
