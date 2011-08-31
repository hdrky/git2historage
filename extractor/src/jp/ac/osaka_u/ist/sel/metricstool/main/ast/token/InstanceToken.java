package jp.ac.osaka_u.ist.sel.metricstool.main.ast.token;


/**
 * ����̃C���X�^���X���w�肷��g�[�N���N���X
 * 
 * @author kou-tngt
 *
 */
public class InstanceToken extends AstTokenAdapter {

    /**
     * �������g���w�肷��萔.
     */
    public static final InstanceToken THIS = new InstanceToken("this");

    /**
     * ��̃C���X�^���X���w�肷��萔.
     */
    public static final InstanceToken NULL = new InstanceToken("null");

    /**
     * 
     * @param text
     */
    protected InstanceToken(final String text) {
        super(text);
    }
}
