using Application.IRepositories.Base;
using Domain.Entities;

namespace Application.IRepositories;

public interface IBrandsRepository : IBaseRepository<Brand>
{
    Task<Brand> UpdateAsync(Brand brand, CancellationToken cancellationToken);
}