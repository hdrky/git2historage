package jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.antlr;


import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Stack;

import jp.ac.osaka_u.ist.sel.metricstool.main.ast.databuilder.ASTParseException;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstToken;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.token.AstTokenTranslator;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitStrategy;
import jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitor;
import jp.ac.osaka_u.ist.sel.metricstool.main.parse.CommonASTWithLineNumber;
import antlr.collections.AST;


/**
 * antlr��AST��K�₷�� {@link AstVisitor}.
 * 
 * 
 * @author kou-tngt
 *
 */
public class AntlrAstVisitor implements AstVisitor<AST> {

    /**
     * ����translator�Ŏw�肳�ꂽ {@link AstTokenTranslator} �ƃf�t�H���g�� {@link AstVisitStrategy}��
     * �ݒ肷��R���X�g���N�^.
     * ���̃R���X�g���N�^���琶�����ꂽ�f�t�H���g��AstVisitStrategy�̓N���X�⃁�\�b�h�����̃m�[�h���K�₷��悤�Ƀr�W�^�[��U������.
     * 
     * @param translator�@���̃r�W�^�[���g�p����AST�m�[�h�̖|��@
     */
    public AntlrAstVisitor(final AstTokenTranslator<AST> translator) {
        this(translator, true, true);
    }

    /**
     * ����translator�Ŏw�肳�ꂽ {@link AstTokenTranslator} �ƃf�t�H���g�� {@link AstVisitStrategy}��
     * �ݒ肷��R���X�g���N�^.
     * 
     * �N���X�⃁�\�b�h�����̃m�[�h��K�₷�邩�ǂ���������intoClass��intoMethod�Ŏw�肷��.
     * 
     * @param translator�@���̃r�W�^�[���g�p����AST�m�[�h�̖|��@
     * @param intoClass �N���X��\��AST�̓�����K�₷�邩�ǂ������w�肷��.�K�₷��ꍇ��true.
     * @param intoMethod�@���\�b�h��\��AST�̓�����K�₷�邩�ǂ������w�肷��.�K�₷��ꍇ��true.
     */
    public AntlrAstVisitor(final AstTokenTranslator<AST> translator, final boolean intoClass,
            final boolean intoMethod) {
        this(translator, new AntlrAstVisitStrategy(intoClass, intoMethod));
    }

    /**
     * �����Ŏw�肳�ꂽ {@link AstTokenTranslator} �� {@link AstVisitStrategy}��
     * �ݒ肷��R���X�g���N�^.
     * 
     * @param translator�@���̃r�W�^�[���g�p����AST�m�[�h�̖|��@
     * @param strategy�@���̃r�W�^�[�̖K����U������AstVisitStrategy�C���X�^���X
     */
    public AntlrAstVisitor(final AstTokenTranslator<AST> translator,
            final AstVisitStrategy<AST> strategy) {
        if (null == translator) {
            throw new NullPointerException("translator is null.");
        }
        if (null == strategy) {
            throw new NullPointerException("starategy is null.");
        }

        this.visitStrategy = strategy;
        this.translator = translator;
    }

    /**
     * ���̃r�W�^�[�����s����e {@link AstVisitEvent} �̒ʒm���󂯂郊�X�i��o�^����.
     * 
     * @param listener �o�^���郊�X�i
     * @throws NullPointerException listener��null�̏ꍇ
     */
    public void addVisitListener(final AstVisitListener listener) {
        if (null == listener) {
            throw new NullPointerException("listener is null.");
        }

        this.listeners.add(listener);
    }

    /**
     * ���̃r�W�^�[�����s����e {@link AstVisitEvent} �̒ʒm���󂯂郊�X�i���폜����.
     * 
     * @param listener�@�폜���郊�X�i
     * @throws NullPointerException listener��null�̏ꍇ
     */
    public void removeVisitListener(final AstVisitListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * ���̃r�W�^�[�̏�Ԃ�������Ԃɖ߂�.
     * �C�x���g���X�i�͍폜����Ȃ�.
     */
    public void reset() {
        this.eventStack.clear();
        this.nodeStack.clear();
    }

    private void printAST(AST node, int nest){
        CommonASTWithLineNumber nextNode = (CommonASTWithLineNumber) node;
        while(null != nextNode){
            CommonASTWithLineNumber currentNode = nextNode;
            nextNode = (CommonASTWithLineNumber) nextNode.getNextSibling();
            AstToken token = this.translator.translate(currentNode);
            for(int i = 0; i < nest; i++){
                System.out.print("  ");
            }
            System.out.println(token.toString() + " (" + currentNode.getText() + ")" + " : " + "[" + currentNode.getFromLine() + ", " + currentNode.getFromColumn() + "]" + "[" + currentNode.getToLine() + ", " + currentNode.getToColumn() + "]");
            printAST(currentNode.getFirstChild(), nest + 1);
        }
    }
    
    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.ast.visitor.AstVisitor#startVisiting(java.lang.Object)
     */
    public void startVisiting(final AST startNode) throws ASTParseException {
        AST nextNode = startNode;
        //printAST(startNode, 0);

        AstToken parentToken = null;
        while (null != nextNode) {
            //���̃m�[�h�̃g�[�N������AstToken�ɕϊ�����
            final AstToken token = this.translator.translate(nextNode);

            //�ʒu��񂪗��p�ł���Ȃ�擾����.
            int startLine = 0;
            int startColumn = 0;
            int endLine = 0;
            int endColumn = 0;
            if (nextNode instanceof CommonASTWithLineNumber) {
                CommonASTWithLineNumber node = (CommonASTWithLineNumber) nextNode;
                startLine = node.getFromLine();
                startColumn = node.getFromColumn();
                endLine = node.getToLine();
                endColumn = node.getToColumn();
            }
            
            //�K��C�x���g���쐬
            final AstVisitEvent event = new AstVisitEvent(this, token, nextNode.getText(), parentToken, startLine, startColumn,
                    endLine, endColumn);

            this.fireVisitEvent(event);

            if (this.visitStrategy.needToVisitChildren(nextNode, event.getToken())) {
                //�q�m�[�h��K�₷��ꍇ

                this.fireEnterEvent(event);
                this.eventStack.push(event);
                this.nodeStack.push(nextNode);
                nextNode = nextNode.getFirstChild();
                
                //�q�m�[�h��K�₷��̂ŁC���݂̃m�[�h���e�m�[�h�ɂȂ�
                parentToken = token;

            } else {
                //���̌Z��ɐi�ޏꍇ
                nextNode = nextNode.getNextSibling();
            }

            if (null == nextNode) {
                //���̍s���悪�Ȃ�

                AstVisitEvent exitedEvent = null;
                
                //�܂��X�^�b�N��k���Ă܂��H���ĂȂ��Z���T��
                while (!this.nodeStack.isEmpty()
                        && null == (nextNode = this.nodeStack.pop().getNextSibling())) {
                    exitedEvent = this.eventStack.pop();
                    this.fireExitEvent(exitedEvent);
                }

                if (!this.eventStack.isEmpty()) {
                    exitedEvent = this.eventStack.pop();
                    this.fireExitEvent(exitedEvent);
                }
                
                if(null != exitedEvent) {
                    parentToken = exitedEvent.getParentToken();
                }
            }
        }
    }

    /**
     * ���݂̃m�[�h�̓����ɓ���C�x���g�𔭍s����
     * @param event�@���s����C�x���g
     */
    private void fireEnterEvent(final AstVisitEvent event) {
        for (final AstVisitListener listener : this.listeners) {
            listener.entered(event);
        }
    }

    /**
     * ���݂̃m�[�h�̓�������o��C�x���g�𔭍s����
     * @param event�@���s����C�x���g
     */
    private void fireExitEvent(final AstVisitEvent event) throws ASTParseException {
        for (final AstVisitListener listener : this.listeners) {
            listener.exited(event);
        }
    }

    /**
     * �m�[�h�ɖK�₷��C�x���g�𔭍s����
     * @param event�@���s����C�x���g
     */
    private void fireVisitEvent(final AstVisitEvent event) {
        for (final AstVisitListener listener : this.listeners) {
            listener.visited(event);
        }
    }

    /**
     * ���̃r�W�^�[�̖K����U������.
     */
    private final AstVisitStrategy<AST> visitStrategy;

    /**
     * �K�₵��AST�m�[�h��AstToken�ɕϊ�����
     */
    private final AstTokenTranslator<AST> translator;

    /**
     * �C�x���g���Ǘ�����X�^�b�N
     */
    private final Stack<AstVisitEvent> eventStack = new Stack<AstVisitEvent>();

    /**
     * �m�[�h���Ǘ�����X�^�b�N
     */
    private final Stack<AST> nodeStack = new Stack<AST>();

    /**
     * �C�x���g�ʒm���󂯎�郊�X�i�[�̃Z�b�g
     */
    private final Set<AstVisitListener> listeners = new LinkedHashSet<AstVisitListener>();

}
