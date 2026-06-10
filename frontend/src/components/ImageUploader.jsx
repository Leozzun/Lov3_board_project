import { useState } from 'react'

export default function ImageUploader({ currentImg, onUploaded, uploadFn }) {
  const [preview, setPreview] = useState(currentImg || '')
  const [uploading, setUploading] = useState(false)

  const handleFile = async (e) => {
    const file = e.target.files[0]
    if (!file) return
    setPreview(URL.createObjectURL(file))
    setUploading(true)
    try {
      const res = await uploadFn(file)
      onUploaded(res.data.imageUrl)
    } catch {
      alert('이미지 업로드에 실패했습니다')
      setPreview(currentImg || '')
    } finally {
      setUploading(false)
    }
  }

  return (
    <div>
      {preview && (
        <div style={{
          width: '100%', height: '160px', marginBottom: '12px',
          background: `url(${preview}) center/cover`,
          border: '1px solid #e8e8e8',
        }} />
      )}
      <label style={{
        display: 'inline-block', padding: '10px 20px',
        border: '1.5px solid #0a0a0a', fontSize: '12px', fontWeight: '700',
        letterSpacing: '0.5px', cursor: 'pointer',
        background: uploading ? '#f0f0f0' : '#fff',
        color: uploading ? '#a0a0a0' : '#0a0a0a',
      }}>
        {uploading ? '업로드 중...' : preview ? '사진 변경' : '사진 선택'}
        <input type="file" accept="image/*" onChange={handleFile} style={{ display: 'none' }} disabled={uploading} />
      </label>
      {preview && !uploading && (
        <span style={{ marginLeft: '12px', fontSize: '12px', color: '#a0a0a0' }}>✓ 업로드 완료</span>
      )}
    </div>
  )
}
