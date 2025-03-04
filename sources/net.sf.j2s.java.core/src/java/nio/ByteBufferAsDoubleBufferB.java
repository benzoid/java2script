package java.nio; // decompiled using http://jd.benow.ca/

class ByteBufferAsDoubleBufferB
  extends DoubleBuffer
{
  protected final ByteBuffer bb;
  protected final int offset;
  
  ByteBufferAsDoubleBufferB(ByteBuffer paramByteBuffer)
  {
    super(-1, 0, paramByteBuffer
      .remaining() >> 3, paramByteBuffer
      .remaining() >> 3);
    this.bb = paramByteBuffer;
    
    int i = capacity();
    limit(i);
    int j = position();
    assert (j <= i);
    this.offset = j;
  }
  
  ByteBufferAsDoubleBufferB(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    super(paramInt1, paramInt2, paramInt3, paramInt4);
    this.bb = paramByteBuffer;
    this.offset = paramInt5;
  }
  
  public DoubleBuffer slice()
  {
    int i = position();
    int j = limit();
    assert (i <= j);
    int k = i <= j ? j - i : 0;
    int m = (i << 3) + this.offset;
    assert (m >= 0);
    return new ByteBufferAsDoubleBufferB(this.bb, -1, 0, k, k, m);
  }
  
  public DoubleBuffer duplicate()
  {
    return new ByteBufferAsDoubleBufferB(this.bb, 
      markValue(), 
      position(), 
      limit(), 
      capacity(), this.offset);
  }
  
  public DoubleBuffer asReadOnlyBuffer()
  {
    return new ByteBufferAsDoubleBufferRB(this.bb, 
      markValue(), 
      position(), 
      limit(), 
      capacity(), this.offset);
  }
  
  protected int ix(int paramInt)
  {
    return (paramInt << 3) + this.offset;
  }
  
  public double get()
  {
    return Bits.getDoubleB(this.bb, ix(nextGetIndex()));
  }
  
  public double get(int paramInt)
  {
    return Bits.getDoubleB(this.bb, ix(checkIndex(paramInt)));
  }
  
  public DoubleBuffer put(double paramDouble)
  {
    Bits.putDoubleB(this.bb, ix(nextPutIndex()), paramDouble);
    return this;
  }
  
  public DoubleBuffer put(int paramInt, double paramDouble)
  {
    Bits.putDoubleB(this.bb, ix(checkIndex(paramInt)), paramDouble);
    return this;
  }
  
  public DoubleBuffer compact()
  {
    int i = position();
    int j = limit();
    assert (i <= j);
    int k = i <= j ? j - i : 0;
    
    ByteBuffer localByteBuffer1 = this.bb.duplicate();
    localByteBuffer1.limit(ix(j));
    localByteBuffer1.position(ix(0));
    ByteBuffer localByteBuffer2 = localByteBuffer1.slice();
    localByteBuffer2.position(i << 3);
    localByteBuffer2.compact();
    position(k);
    limit(capacity());
    discardMark();
    return this;
  }
  
  public boolean isDirect()
  {
    return this.bb.isDirect();
  }
  
  public boolean isReadOnly()
  {
    return false;
  }
  
  public ByteOrder order()
  {
    return ByteOrder.BIG_ENDIAN;
  }
}
