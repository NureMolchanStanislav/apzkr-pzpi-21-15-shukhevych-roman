using System.Linq.Expressions;
using Domain.Common;
using MongoDB.Bson;

namespace Application.IRepositories.Base;

public interface IBaseRepository<TEntity> where TEntity : EntityBase
{
    Task<TEntity> AddAsync(TEntity entity, CancellationToken cancellationToken);

    Task<TEntity> GetOneAsync(ObjectId id, CancellationToken cancellationToken);

    Task<TEntity> GetOneAsync(Expression<Func<TEntity, bool>> predicate, CancellationToken cancellationToken);

    Task<List<TEntity>> GetPageAsync(int pageNumber, int pageSize, CancellationToken cancellationToken);

    Task<List<TEntity>> GetPageAsync(int pageNumber, int pageSize, Expression<Func<TEntity, bool>> predicate, CancellationToken cancellationToken);

    Task<bool> ExistsAsync(Expression<Func<TEntity, bool>> predicate, CancellationToken cancellationToken);

    Task<int> GetTotalCountAsync();

    Task<int> GetCountAsync(Expression<Func<TEntity, bool>> predicate, CancellationToken cancellationToken);

    Task<TEntity> DeleteAsync(TEntity entity, CancellationToken cancellationToken);

    Task<List<TEntity>> GetAllAsync(CancellationToken cancellationToken);

    Task<List<TEntity>> GetAllAsync(Expression<Func<TEntity, bool>> predicate, CancellationToken cancellationToken);

    Task<List<TEntity>> GetPageAllAsync(int pageNumber, int pageSize, CancellationToken cancellationToken);
}