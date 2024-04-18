namespace Application.IServices;

public interface IRFIDTagsService
{
    Task<bool> UpdateStatus(bool status, CancellationToken cancellationToken);
}