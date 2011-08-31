package jp.ac.osaka_u.ist.sel.metricstool.main.io;


/**
 * {@link ProgressReporter}�̃f�t�H���g���� �R���X�g���N�^�� {@link ProgressSource} ��^����. ���� {@link ProgressSource}
 * �C���X�^���X����C���̃N���X�̃C���X�^���X�𕡐���邱�Ƃ͂ł��Ȃ�
 * 
 * @author kou-tngt
 * 
 */
public class DefaultProgressReporter implements ProgressReporter {

    /**
     * �B��̃R���X�g���N�^. ������ {@link ProgressSource} ���󂯎��C�񍐗p�̐ڑ����m������
     * 
     * @param source �i���񍐂�����v���O�C��
     * @throws AlreadyConnectedException ���łɓ���{@link ProgressSource}���ʂ̃��|�[�^�[�Őڑ�������Ă���ꍇ
     */
    public DefaultProgressReporter(final ProgressSource source) throws AlreadyConnectedException {
        // ���̃\�[�X�p�̃R�l�N�^������āC�ڑ�
        this.connector = ProgressConnector.getConnector(source);
        this.connector.connect(this);
    }

    /**
     * �i����񑗐M�̏I����񍐂��郁�\�b�h
     */
    public void reportProgressEnd() {
        if (null != this.connector) {
            this.connector.progressEnd();
            this.connector = null;
        }
    }

    /**
     * �i������񍐂��郁�\�b�h
     * 
     * @param percentage �i���l�i%�j
     * @throws IllegalArgumentException percentage��0-100�̊Ԃɓ����ĂȂ��ꍇ
     * @throws IllegalStateException percentage���O��񍐂����l��艺�������ꍇ
     * @see ProgressReporter#reportProgress(int)
     */
    public void reportProgress(final int percentage) {
        if (0 > percentage || 100 < percentage) {
            throw new IllegalArgumentException("reported value " + percentage
                    + " was out of range(0-100).");
        }

        if (percentage < this.previousValue) {
            // �O��̕񍐂��l��������
            throw new IllegalStateException("reported value was decreased.");
        }

        if (null != this.connector) {
            try {
                this.previousValue = percentage;
                this.connector.reportProgress(percentage);
            } catch (final ConnectionException e) {
                // �ڑ�����ĂȂ������ʌ��������X���b�h�ɐؒf���ꂽ���v���O�C�����s�X���b�h���߁X�i�����j�I���������ʒm���Ȃ�
                this.connector = null;
            }
        }
    }

    /**
     * ���̃��|�[�^�[�̐ڑ���
     */
    private ProgressConnector connector;

    /**
     * 1��O�ɂɕ񍐂����i�����l
     */
    private int previousValue;

}
