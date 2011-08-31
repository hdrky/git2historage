package jp.ac.osaka_u.ist.sel.metricstool.main.parse;


import java.util.Map;
import java.util.WeakHashMap;


/**
 * AST��̊e�v�f�̊J�n�s�C�J�n��C�I���s�C�I������Ǘ�����N���X.
 * 
 * @author kou-tngt
 *
 */
public class DefaultPositionManager implements PositionManager {

    /**
     * ����key�̊J�n�s��Ԃ�.
     * �o�^����Ă��Ȃ��ꍇ��0��Ԃ�.
     * @param key �J�n�s���擾�������v�f
     */
    public int getStartLine(final Object key) {
        if (this.dataMap.containsKey(key)) {
            return this.getLineColumn(key).getStartLine();
        } else {
            return 0;
        }
    }

    /**
     * ����key�̊J�n���Ԃ�.
     * �o�^����Ă��Ȃ��ꍇ��0��Ԃ�.
     * @param key �J�n����擾�������v�f
     */
    public int getStartColumn(final Object key) {
        if (this.dataMap.containsKey(key)) {
            return this.getLineColumn(key).getStartColumn();
        } else {
            return 0;
        }
    }

    /**
     * ����key�̏I���s��Ԃ�.
     * �o�^����Ă��Ȃ��ꍇ��0��Ԃ�.
     * @param key �I���s���擾�������v�f
     */
    public int getEndLine(final Object key) {
        if (this.dataMap.containsKey(key)) {
            return this.getLineColumn(key).getEndLine();
        } else {
            return 0;
        }
    }

    /**
     * ����key�̏I�����Ԃ�.
     * �o�^����Ă��Ȃ��ꍇ��0��Ԃ�.
     * @param key �I������擾�������v�f
     */
    public int getEndColumn(final Object key) {
        if (this.dataMap.containsKey(key)) {
            return this.getLineColumn(key).getEndColumn();
        } else {
            return 0;
        }
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager#setStartLine(java.lang.Object, int)
     */
    public void setStartLine(final Object key, final int line) {
        this.getLineColumn(key).setStartLine(line);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager#setStartColumn(java.lang.Object, int)
     */
    public void setStartColumn(final Object key, final int column) {
        this.getLineColumn(key).setStartLine(column);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager#setEndLine(java.lang.Object, int)
     */
    public void setEndLine(final Object key, final int line) {
        this.getLineColumn(key).setEndLine(line);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager#setEndColumn(java.lang.Object, int)
     */
    public void setEndColumn(final Object key, final int column) {
        this.getLineColumn(key).setEndLine(column);
    }

    /* (non-Javadoc)
     * @see jp.ac.osaka_u.ist.sel.metricstool.main.parse.PositionManager#setPosition(java.lang.Object, int, int, int, int)
     */
    public void setPosition(final Object key, final int startLine, final int startColumn,
            final int endLine, final int endColumn) {
        final Position position = this.getLineColumn(key);
        position.setStartLine(startLine);
        position.setStartColumn(startColumn);
        position.setEndLine(endLine);
        position.setEndColumn(endColumn);
    }

    /**
     * ����key�̍s�ԍ������L�^����C���X�^���X��Ԃ�.
     * ���ł�key�ɑΉ�����C���X�^���X���쐬����Ă���΂����Ԃ��C�܂��쐬����Ă��Ȃ���ΐV���ɍ쐬���ĕԂ�.
     * @param key �����L�^����C���X�^���X���쐬�������v�f
     * @return ����key�ɑΉ�����s�ԍ����L�^�C���X�^���X
     */
    private Position getLineColumn(final Object key) {
        if (null == key) {
            throw new NullPointerException("key is null");
        }

        if (this.dataMap.containsKey(key)) {
            return this.dataMap.get(key);
        } else {
            final Position newInstance = new Position();
            this.dataMap.put(key, newInstance);
            return newInstance;
        }
    }

    /**
     * �s�Ɨ�̏����L�^��������N���X
     * 
     * @author kou-tngt
     *
     */
    private static class Position {
        /**
         * �I�����Ԃ�.
         * @return �I����
         */
        public int getEndColumn() {
            return this.endColumn;
        }

        /**
         * �I���s��Ԃ�
         * @return�@�I���s
         */
        public int getEndLine() {
            return this.endLine;
        }

        /**
         * �J�n���Ԃ�
         * @return�@�J�n��
         */
        public int getStartColumn() {
            return this.startColumn;
        }

        /**
         * �J�n�s��Ԃ�
         * @return�@�J�n�s
         */
        public int getStartLine() {
            return this.startLine;
        }

        /**
         * �I������Z�b�g����
         * @param endColumn�@�I����
         */
        public void setEndColumn(final int endColumn) {
            this.endColumn = endColumn;
        }

        /**
         * �I���s���Z�b�g����
         * @param endLine�@�I���s
         */
        public void setEndLine(final int endLine) {
            this.endLine = endLine;
        }

        /**
         * �J�n����Z�b�g����
         * @param startColumn�@�J�n��
         */
        public void setStartColumn(final int startColumn) {
            this.startColumn = startColumn;
        }

        /**
         * �J�n�s���Z�b�g����
         * @param startLine�@�J�n�s
         */
        public void setStartLine(final int startLine) {
            this.startLine = startLine;
        }

        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return this.startLine + "." + this.startColumn + " - " + this.endLine + "."
                    + this.endColumn;
        }

        /**
         * �J�n�s
         */
        private int startLine;

        /**
         * �J�n��
         */
        private int startColumn;

        /**
         * �I���s
         */
        private int endLine;

        /**
         * �I����
         */
        private int endColumn;
    }

    private final Map<Object, Position> dataMap = new WeakHashMap<Object, Position>();
}
