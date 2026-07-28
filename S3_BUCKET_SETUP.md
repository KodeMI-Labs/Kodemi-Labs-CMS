# S3 Bucket Setup Guide for Blog Thumbnails

## 1. Create S3 Bucket

### Login to AWS Console
- **Console URL**: https://528701450746.signin.aws.amazon.com/console
- **Username**: BackendTeam
- **Password**: KodemiBackend02

### Create Bucket
1. Go to **S3 Console**: https://s3.console.aws.amazon.com/s3/
2. Click **"Create bucket"**
3. Configure:
   - **Bucket name**: `cms-thumbnails-backend` (must be globally unique, change if needed)
   - **AWS Region**: `ap-south-1` (Asia Pacific Mumbai) — **MUST match your existing setup**
   - **Object Ownership**: ACLs enabled → Object writer
   - **Block Public Access settings**: 
     - ❌ Uncheck "Block all public access"
     - ✅ Check the acknowledgment box
   - **Bucket Versioning**: Disabled (or Enable if you want version history)
   - **Tags**: (Optional) Add tags like `Project: CMS`, `Purpose: Thumbnails`
   - **Default encryption**: Enable (recommended) → Amazon S3-managed keys (SSE-S3)
4. Click **"Create bucket"**

## 2. Configure Bucket Policy for Public Read

After bucket creation:

1. Click on your new bucket name
2. Go to **"Permissions"** tab
3. Scroll to **"Bucket policy"** section
4. Click **"Edit"**
5. Paste this policy (replace `cms-thumbnails-backend` with your actual bucket name):

```json
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Sid": "PublicReadGetObject",
            "Effect": "Allow",
            "Principal": "*",
            "Action": "s3:GetObject",
            "Resource": "arn:aws:s3:::cms-thumbnails-backend/*"
        }
    ]
}
```

6. Click **"Save changes"**

## 3. Configure CORS (if frontend uploads directly)

If your frontend will upload directly to S3, configure CORS:

1. In bucket → **"Permissions"** tab
2. Scroll to **"Cross-origin resource sharing (CORS)"**
3. Click **"Edit"**
4. Paste this configuration:

```json
[
    {
        "AllowedHeaders": ["*"],
        "AllowedMethods": ["GET", "PUT", "POST", "DELETE"],
        "AllowedOrigins": [
            "http://localhost:5173",
            "http://localhost:3000",
            "https://effective-chunk-unwound.ngrok-free.dev",
            "https://blah-hypnotist-tamper.ngrok-free.dev",
            "https://paralyses-subfloor-overdraft.ngrok-free.dev",
            "https://thinner-crudely-bunkmate.ngrok-free.dev",
            "https://garage-army-vivacious.ngrok-free.dev"
        ],
        "ExposeHeaders": ["ETag"],
        "MaxAgeSeconds": 3000
    }
]
```

5. Click **"Save changes"**

## 4. Update Application Configuration

### Update `.env` or `application.yaml`

Set the S3 bucket name in your configuration:

**Option A: Using `.env` file**
```properties
AWS_S3_BUCKET=cms-thumbnails-backend
```

**Option B: Directly in `application.yaml`**
```yaml
aws:
  s3:
    bucket: cms-thumbnails-backend
```

## 5. Test the Upload Endpoint

Once configured, test the thumbnail upload:

### Using cURL:
```bash
curl -X POST http://localhost:8087/blog/upload-thumbnail \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -F "file=@/path/to/image.jpg"
```

### Expected Response:
```json
{
  "thumbnailUrl": "https://cms-thumbnails-backend.s3.ap-south-1.amazonaws.com/blog-thumbnails/uuid.jpg"
}
```

## 6. Using with Blog Creation

### Step 1: Upload thumbnail
```bash
POST /blog/upload-thumbnail
Content-Type: multipart/form-data

file: [binary image data]
```

**Response:**
```json
{
  "thumbnailUrl": "https://cms-thumbnails-backend.s3.ap-south-1.amazonaws.com/blog-thumbnails/abc-123.jpg"
}
```

### Step 2: Create blog with thumbnail URL
```bash
POST /blog/create-dto
Content-Type: application/json

{
  "title": "My Blog Post",
  "content": "Blog content here...",
  "author": "John Doe",
  "category": "Technology",
  "tags": ["tech", "programming"],
  "thumbnail": "https://cms-thumbnails-backend.s3.ap-south-1.amazonaws.com/blog-thumbnails/abc-123.jpg",
  "status": "published"
}
```

## Security Notes

✅ **Configured**: IAM user credentials with S3 write permissions
✅ **Configured**: DynamoDB access
⚠️ **Review**: Public read access on bucket — ensure this aligns with your security requirements
⚠️ **Review**: Consider adding bucket lifecycle policies to auto-delete old unused thumbnails

## Troubleshooting

### Issue: "Access Denied" when uploading
- Verify IAM user `your_aws_access_key_here` has `s3:PutObject` permission
- Check bucket policy allows your IAM user to write

### Issue: Images not publicly accessible
- Verify "Block Public Access" is disabled
- Verify bucket policy has `s3:GetObject` for `Principal: "*"`

### Issue: CORS errors from frontend
- Ensure CORS configuration includes your frontend domain
- Check browser console for specific CORS error messages

## Alternative Bucket Names

If `cms-thumbnails-backend` is taken, try:
- `kodemi-cms-thumbnails`
- `cms-backend-media-{random-number}`
- `kodemi-blog-images`

Remember to update `application.yaml` and `.env.example` with the final bucket name!
